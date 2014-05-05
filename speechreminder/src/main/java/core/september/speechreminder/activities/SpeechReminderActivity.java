/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.september.speechreminder.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.revmob.RevMob;
import com.revmob.ads.banner.RevMobBanner;
import com.revmob.ads.fullscreen.RevMobFullscreen;

import java.util.List;

import core.september.android.basement.AbstractNavigationDrawerActivity;
import core.september.speechreminder.R;
import core.september.speechreminder.activities.adapters.LeftMenuAdapter;
import core.september.speechreminder.activities.fragments.ListItemFragment;
import core.september.speechreminder.activities.fragments.ManageItemFragment;
import core.september.speechreminder.config.Config;
import core.september.speechreminder.helpers.CRUD;
import core.september.speechreminder.models.Event;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class SpeechReminderActivity extends AbstractNavigationDrawerActivity implements ListItemFragment.UpdateListener {

    private final static String EVENT_KEY = "EVENT_KEY";
    private final int RESULT_SETTINGS = 1;
    private boolean mTwoPane;
    private Event selectedEvent;
    private RevMobFullscreen fullscreen;

    @Override
    protected int contentView() {
        return R.layout.speechreminder_main;
    }

    @Override
    protected int drawerLayout() {
        return R.id.drawer_layout;
    }

    @Override
    protected int leftDrawer() {
        return R.id.left_drawer;
    }

    @Override
    protected ArrayAdapter mDrawerListAdapter() {
        ArrayAdapter adapter = new LeftMenuAdapter(this, R.layout.left_drawer_row, new Integer[]{0, 1, 2});
        return adapter;
    }


    @Override
    protected int drawerImageRes() {
        return R.drawable.ic_navigation_drawer;

    }

    @Override
    protected int openDrawerContentDescRes() {
        return R.string.drawer_open;
    }

    @Override
    protected int closeDrawerContentDescRes() {
        return R.string.drawer_close;
    }

    @Override
    protected int drawerShadow() {
        return R.drawable.drawer_shadow;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.speechreminder_main);

       /* ListItemFragment firstFragment = new ListItemFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_item_fragment, firstFragment).commit();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        //setContentView(R.layout.main);

        RevMob revmob = RevMob.start(this, Config.REVMOB_APP_ID);
        RevMobBanner banner = revmob.createBanner(this);
        fullscreen = revmob.createFullscreen(this, null); // pre-load it without showing it
        if(findViewById(R.id.banner) != null) {
            ViewGroup view = (ViewGroup) findViewById(R.id.banner);
            view.addView(banner);
        }

    }

    @Override
    protected boolean handleNavigationButton(MenuItem item, FallBackDefault fallBackDefault) {
        // Handle action buttons
        switch (item.getItemId()) {
            case 0:
                /*// create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }*/
                return true;
            default:
                return fallBackDefault.doDefault();
        }
    }

    @Override
    protected void selectItem(int position) {
        // update the main content by replacing fragments
/*        switch (position) {
            case LIST_ACTIVITY:

                break;
        }
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);*/
        switch (position) {
            case 0: //Settinfgs
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
            case 1: //simple notification test
                //SpeechReminder.getInstance().notifyOnBar(this.getClass(),"Simple title","simple notification");
                fullscreen.show();
                break;
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                List<Event> eventList = CRUD.getInstance().select(Event.class);
                for (Event event : eventList) {
                    event.assign();
                }
                break;

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putLong(Conf, selectedEvent == null ? -1 : selectedEvent.get_id());
    }

    @Override
    public void onUpdate(long id) {
        ListItemFragment fragment = (ListItemFragment) getSupportFragmentManager().findFragmentById(R.id.list_item_fragmet);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
        List<Event> eventList = CRUD.getInstance().select(Event.class);
        fragment.showDetails(id);
    }

    @Override
    public void onElementClicked(long id) {
        ListItemFragment fragment = (ListItemFragment) getSupportFragmentManager().findFragmentById(R.id.list_item_fragmet);
        fragment.showDetails(id);
    }




   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    public static class DetailsActivity extends ActionBarActivity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

            int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
            if (actionBarTitleId > 0) {
                TextView title = (TextView) findViewById(actionBarTitleId);
                if (title != null) {
                    title.setTextColor(getResources().getColor(R.color.WhiteSmoke));
                }
            }

           /* if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                // If the screen is now in landscape mode, we can show the
                // dialog in-line with the list so we don't need this activity.
                finish();
                return;
            }*/

            //long id = getIntent().getLongExtra(Config.EXTRA_FIELD);
            // During initial setup, plug in the details fragment.
            ManageItemFragment manageItem = new ManageItemFragment();
            manageItem.setArguments(getIntent().getExtras());
            //manageItem.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, manageItem).commit();

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent upIntent = new Intent(DetailsActivity.this, SpeechReminderActivity.class);
                    if (NavUtils.shouldUpRecreateTask(DetailsActivity.this, upIntent)) {
                        // This activity is not part of the application's task, so create a new task
                        // with a synthesized back stack.
                        //startActivity(upIntent);
                        TaskStackBuilder.create(DetailsActivity.this)
                                .addNextIntent(upIntent)
                                .startActivities();
                        finish();
                    } else {
                        // This activity is part of the application's task, so simply
                        // navigate up to the hierarchical parent activity.
                        NavUtils.navigateUpTo(this, upIntent);
                    }
                    return true;
            }
            return super.onOptionsItemSelected(item);

        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
	*/

}
