package com.buddybuild.rest;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RestCoordinatorTest {

    private DashboardWebService mockDashboardWebService;
    private ApiWebService mockApiWebService;
    private TokenStore spyTokenStore;
    private BehaviorDelegate<DashboardWebService> dashboardWebServiceBehaviorDelegate;

    @Before
    public void setUp() throws Exception {
        Retrofit dashboardRetrofit = new Retrofit.Builder()
                .baseUrl("https://dashboard.buddybuild.com/")
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
    }

    @Test
    public void whenLoggingIn_ShouldUpdateTokenStore() throws Exception {
        // arrange
        String email = "a@b.com";
        String password = "password";

        String json = JsonStringReader.readJsonSampleFromFile("login/valid_login_response");
        Gson gson = new Gson();
        LoginResponseBody loginResponseBody = gson.fromJson(json, LoginResponseBody.class);

        when(mockDashboardWebService.login(Mockito.any(LoginRequestBody.class)))
                .thenReturn(
                        dashboardWebServiceBehaviorDelegate
                                .returningResponse(loginResponseBody)
                                .login(new LoginRequestBody(email, password))
                );

        RestCoordinator restCoordinator
                = new RestCoordinator(mockApiWebService, mockDashboardWebService, spyTokenStore);

        // act
        TestObserver<LoginResult> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
        verify(spyTokenStore).setToken("some returned token");
    }
}
