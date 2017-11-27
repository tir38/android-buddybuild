package com.buddybuild.rest;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link LiveRestCoordinator}
 */
public class LiveRestCoordinatorTest {

    private DashboardWebService mockDashboardWebService;
    private TokenStore mockTokenStore;
    private BehaviorDelegate<DashboardWebService> dashboardWebServiceBehaviorDelegate;
    private Gson gson;
    private ApiConstants stubApiConstants;

    @Before
    public void setUp() throws Exception {
        stubApiConstants = () -> "https://stuburl.com/";

        // TODO if this is really going to be unit tests, we shouldn't mock retrofit with MockRetrofit, we should
        // just mock our own interface DashboardWebService
        Retrofit dashboardRetrofit = new Retrofit.Builder()
                .baseUrl(stubApiConstants.getBaseDashboardUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // This is the magic....
                // we can't use createWithScheduler(Schedulers.io())
                .build();

        NetworkBehavior perfectNetworkBehavior = NetworkBehavior.create();
        perfectNetworkBehavior.setFailurePercent(0);
        perfectNetworkBehavior.setVariancePercent(0);
        perfectNetworkBehavior.setErrorPercent(0);
        perfectNetworkBehavior.setDelay(0, TimeUnit.MICROSECONDS);

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(dashboardRetrofit)
                .networkBehavior(perfectNetworkBehavior)
                .build();

        dashboardWebServiceBehaviorDelegate = mockRetrofit.create(DashboardWebService.class);
        mockDashboardWebService = Mockito.mock(DashboardWebService.class);
        mockTokenStore = Mockito.mock(TokenStore.class);

        gson = new Gson();
    }

    @Test
    public void whenLoggingIn_ShouldUpdateTokenStore() throws Exception {
        // arrange
        String email = "a@b.com";
        String password = "password";

        // build success response
        String json = JsonStringReader.readJsonSampleFromFile("login/valid_login_response");
        LoginResponseBody loginResponseBody = gson.fromJson(json, LoginResponseBody.class);

        when(mockDashboardWebService.login(Mockito.any(LoginRequestBody.class)))
                .thenReturn(
                        dashboardWebServiceBehaviorDelegate
                                .returningResponse(loginResponseBody)
                                .login(new LoginRequestBody(email, password))
                );

        TokenStore spyTokenStore = Mockito.spy(TokenStore.class);

        LiveRestCoordinator restCoordinator = new LiveRestCoordinator(mockDashboardWebService, spyTokenStore,
                stubApiConstants);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        String expectedToken = "some returned token";
        testObserver.assertNoErrors();
        testObserver.assertResult(new LoginResult(LoginResult.Result.SUCCESS, expectedToken));
        verify(spyTokenStore).setToken(expectedToken);
    }

    @Test
    public void whenLoggingIn_ShouldHandleUnknownEmailError() throws Exception {
        // arrange
        String email = "a@b.com";
        String password = "password";

        // build error response
        String json = JsonStringReader.readJsonSampleFromFile("login/error_login_unknown_email");
        Response<Object> response = Response.error(401, ResponseBody.create(MediaType.parse("application/json"), json));

        when(mockDashboardWebService.login(Mockito.any(LoginRequestBody.class)))
                .thenReturn(
                        dashboardWebServiceBehaviorDelegate
                                .returning(Calls.response(response))
                                .login(new LoginRequestBody(email, password))
                );

        LiveRestCoordinator restCoordinator = new LiveRestCoordinator(mockDashboardWebService, mockTokenStore,
                stubApiConstants);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertResult(new LoginResult(LoginResult.Result.UNKNOWN_EMAIL, null));
    }

    @Test
    public void whenLoggingIn_ShouldHandleMisMatchedEmailError() throws Exception {
        // arrange
        String email = "a@b.com";
        String password = "password";

        // build error response
        String json = JsonStringReader.readJsonSampleFromFile("login/error_login_mismatched_email");
        Response<Object> response = Response.error(401, ResponseBody.create(MediaType.parse("application/json"), json));

        when(mockDashboardWebService.login(Mockito.any(LoginRequestBody.class)))
                .thenReturn(
                        dashboardWebServiceBehaviorDelegate
                                .returning(Calls.response(response))
                                .login(new LoginRequestBody(email, password))
                );

        LiveRestCoordinator restCoordinator = new LiveRestCoordinator(mockDashboardWebService, mockTokenStore,
                stubApiConstants);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertResult(new LoginResult(LoginResult.Result.EMAIL_PASSWORD_MISMATCH, null));
    }

    @Test
    public void whenLoggingIn_ShouldHandleThrottleLimitError() throws Exception {
        // arrange
        String email = "a@b.com";
        String password = "password";

        // build error response
        String json = JsonStringReader.readJsonSampleFromFile("login/error_login_throttle_limit");
        Response<Object> response = Response.error(401, ResponseBody.create(MediaType.parse("application/json"), json));

        when(mockDashboardWebService.login(Mockito.any(LoginRequestBody.class)))
                .thenReturn(
                        dashboardWebServiceBehaviorDelegate
                                .returning(Calls.response(response))
                                .login(new LoginRequestBody(email, password))
                );

        LiveRestCoordinator restCoordinator = new LiveRestCoordinator(mockDashboardWebService, mockTokenStore,
                stubApiConstants);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertResult(new LoginResult(LoginResult.Result.THROTTLE_LIMIT, null));
    }
}
