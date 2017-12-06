package com.buddybuild.ui;

import com.buddybuild.Coordinator;
import com.buddybuild.DemoManager;
import com.buddybuild.NoDependencyInjectionApplication;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import javax.inject.Inject;

import toothpick.testing.ToothPickRule;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Unit tests for {@link com.buddybuild.ui.LoginFragment}
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = NoDependencyInjectionApplication.class, packageName = "com.buddybuild")
public class LoginFragmentTest2 {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ToothPickRule toothPickRule = new ToothPickRule(this, "LOGIN FRAGMENT SCOPE");

    @Mock
    Coordinator coordinator;
    @Mock
    DemoManager demoManager;
    @Inject
    LoginFragment loginFragment;

    @Before
    public void setUp() {
        toothPickRule.inject(this);
    }

    /**
     * A helper test to ensure that Robolectric is working right
     */
    @Test
    public void robolectricCanStartFragment() throws Exception {
        // arrange

        // act
        SupportFragmentTestUtil.startFragment(loginFragment);

        // assert
        assertThat(loginFragment.isResumed()).isTrue();
    }
}
