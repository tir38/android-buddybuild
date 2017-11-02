package com.buddybuild.rest;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import io.appflate.restmock.RESTMockFileParser;
import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.RESTMockServerStarter;
import io.appflate.restmock.android.AndroidAssetsFileParser;
import io.appflate.restmock.utils.RequestMatchers;
import io.appflate.restmock.utils.RestMockUtils;
import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;

import static org.assertj.core.api.Java6Assertions.fail;

/**
 * Integration tests for entire "rest" module. Mock the very "edge" of module, by using RestMock to mock OkHttp layer.
 * Then test that the surface (i.e. RestCoordinator returned from RestModule) behaves as expected.
 */
@RunWith(AndroidJUnit4.class)
public class IntegrationTests {

    @Inject
    RestCoordinator restCoordinator;

    private RESTMockFileParser restMockFileParser;

    @Before
    public void setUp() throws Exception {
        MockApplication context = (MockApplication) InstrumentationRegistry.getContext().getApplicationContext();

        // we need to start server before setting up D.I.
        restMockFileParser = new AndroidAssetsFileParser(context);
        RESTMockServerStarter.startSync(restMockFileParser);

        context.getMockComponent().inject(this);
    }

    @Test
    public void whenLoggingIn_EmitLoginResult() throws Exception {
        // arrange
        String email = "a@b.com";
        String password = "password";

        MockResponse mockResponse = RestMockUtils.createResponseFromFile(restMockFileParser,
                "login/valid_login_response.json", 200);
        mockResponse.addHeader("content-type", "application/json");
        RESTMockServer.whenPOST(RequestMatchers.pathContains("auth/local/login"))
                .thenReturn(mockResponse);

        // act
        TestObserver<LoginResult> testObserver = restCoordinator.login(email, password)
                .test();

        // assert
        boolean terminated = testObserver.awaitTerminalEvent();
        if (!terminated) {
            fail("TestObserver timed out before terminating event");
        }

        String expectedToken = "some returned token";
        testObserver.assertNoErrors()
                .assertResult(new LoginResult(LoginResult.Result.SUCCESS, expectedToken));
    }
}
