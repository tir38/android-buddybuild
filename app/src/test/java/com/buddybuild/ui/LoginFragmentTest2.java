package com.buddybuild.ui;

import com.buddybuild.NoDependencyInjectionApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Unit tests for {@link com.buddybuild.ui.LoginFragment}
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = NoDependencyInjectionApplication.class, packageName = "com.buddybuild")
public class LoginFragmentTest2 {

    /**
     * A helper test to ensure that Robolectric is working right
     */
    @Test
    public void robolectricCanStartFragment() throws Exception {
        // arrange
        LoginFragment loginFragment = LoginFragment.newInstance();

        // act
        SupportFragmentTestUtil.startFragment(loginFragment);

        // assert
        assertThat(loginFragment.isResumed()).isTrue();
    }
}
