package alexrnov.cosmichunter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import alexrnov.cosmichunter.activities.SettingsActivity;

public class SettingsActivityTest {

  private SettingsActivity settingsActivity;
  @Before
  public void setUp() throws Exception {
    settingsActivity = new SettingsActivity();
  }

  @After
  public void tearDown() throws Exception {
    settingsActivity = null;
  }

  @Test
  public void onCheckedChanged() {
  }
}