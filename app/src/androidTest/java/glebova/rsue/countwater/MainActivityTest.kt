package glebova.rsue.countwater

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import glebova.rsue.countwater.ui.pofile.ProfileFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun test_onView_ProfileFragment() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.authFragment))
        val fragmentArgs = bundleOf("selectedListItem" to 0)
        val scenario = launchFragmentInContainer<ProfileFragment>(fragmentArgs)
        onView(withId(R.id.fio))
        onView(withId(R.id.address))
        onView(withId(R.id.telephone))
        onView(withId(R.id.settings))
    }

    @Test
    fun test_clickOnSettings() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.authFragment))
        val fragmentArgs = bundleOf("selectedListItem" to 0)
        val scenario = launchFragmentInContainer<ProfileFragment>(fragmentArgs)
        onView(withId(R.id.settings)).perform(click())
        onView(withId(R.id.settingsFragment))

    }
}