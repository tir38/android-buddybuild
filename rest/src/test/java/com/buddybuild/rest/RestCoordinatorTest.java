package com.buddybuild.rest;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public class RestCoordinatorTest {

    private DashboardWebService mockDashboardWebService;
    private ApiWebService mockApiWebService;
    private TokenStore spyTokenStore;

    static final class MockDashboardWebService implements DashboardWebService {

        private BehaviorDelegate<DashboardWebService> behaviorDelegate;

        public MockDashboardWebService(BehaviorDelegate<DashboardWebService> behaviorDelegate) {
            this.behaviorDelegate = behaviorDelegate;
        }

        @Override
        public Single<Response<LoginResponseBody>> login(LoginRequestBody loginRequestBody) {
            String json = JsonStringReader.readJsonSampleFromFile("login/valid_login_response");
            Gson gson = new Gson();
            LoginResponseBody loginResponseBody = gson.fromJson(json, LoginResponseBody.class);
            return behaviorDelegate.returningResponse(loginResponseBody).login(loginRequestBody);
        }
    }

    @Before
    public void setUp() throws Exception {
        Retrofit dashboardRetrofit = new Retrofit.Builder()
                .baseUrl("https://dashboard.buddybuild.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // This is the magic.... we can't use createWithScheduler(Schedulers.io())
                .build();

        NetworkBehavior perfectNetworkBehavior = NetworkBehavior.create();
        perfectNetworkBehavior.setFailurePercent(0);
        perfectNetworkBehavior.setVariancePercent(0);
        perfectNetworkBehavior.setErrorPercent(0);
        perfectNetworkBehavior.setDelay(0, TimeUnit.MICROSECONDS);

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(dashboardRetrofit)
                .networkBehavior(perfectNetworkBehavior)
                .build();

        BehaviorDelegate<DashboardWebService> behaviorDelegate = mockRetrofit.create(DashboardWebService.class);

        mockDashboardWebService = new MockDashboardWebService(behaviorDelegate);

        mockApiWebService = Mockito.mock(ApiWebService.class);
        spyTokenStore = Mockito.spy(TokenStore.class);
    }

    @Test
    public void whenLoggingIn_ShouldUpdateTokenStore() throws Exception {
        // arrange
        String email = "a@b.com";
        String password = "password";

        RestCoordinator restCoordinator
                = new RestCoordinator(mockApiWebService, mockDashboardWebService, spyTokenStore);

        // act
        TestObserver<Boolean> testObserver = new TestObserver<>();
        restCoordinator.login(email, password)
                .subscribe(testObserver);

        // assert
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
        Mockito.verify(spyTokenStore).setToken("some returned token");
    }
}
