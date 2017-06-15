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
    private ApiWebService mockApiWebService;
    private TokenStore spyTokenStore;
    private BehaviorDelegate<DashboardWebService> dashboardWebServiceBehaviorDelegate;
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        Retrofit dashboardRetrofit = new Retrofit.Builder()
                .baseUrl(RestModule.DASHBOARD_URL)
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
        mockApiWebService = Mockito.mock(ApiWebService.class);
        spyTokenStore = Mockito.spy(TokenStore.class);

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

        LiveRestCoordinator restCoordinator
                = new LiveRestCoordinator(mockApiWebService, mockDashboardWebService, spyTokenStore);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
        verify(spyTokenStore).setToken("some returned token");
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

        LiveRestCoordinator restCoordinator
                = new LiveRestCoordinator(mockApiWebService, mockDashboardWebService, spyTokenStore);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertResult(LoginResult.UNKNOWN_EMAIL);
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

        LiveRestCoordinator restCoordinator
                = new LiveRestCoordinator(mockApiWebService, mockDashboardWebService, spyTokenStore);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertResult(LoginResult.EMAIL_PASSWORD_MISMATCH);
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

        LiveRestCoordinator restCoordinator
                = new LiveRestCoordinator(mockApiWebService, mockDashboardWebService, spyTokenStore);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertResult(LoginResult.THROTTLE_LIMIT);
    }
}
