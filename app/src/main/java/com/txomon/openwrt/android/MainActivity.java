package com.txomon.openwrt.android;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

//import android.widget.Toolbar;

import com.txomon.openwrt.ubusrpc.UbusClient;
import com.txomon.openwrt.ubusrpc.UbusRpcClient;
import com.txomon.openwrt.ubusrpc.UbusRpcException;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends CustomCallFragment
        implements UbusRpcFragmentInteractionListenerInterface,
        ObjectExploreFragment.ObjectExploreFragmentInteractionListenerInterface {


    private static final String TAG = "OpenwrtMainActivity";
    private String currentUrl;
    private UbusClient currentClient;

    public UbusClient getCurrentClient() {
        return currentClient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View localView = (View) this.getView();

        //setContentView(R.layout.activity_main); //JAE - Couldn't find this method to execute it
        //Toolbar toolbar = (Toolbar) localView.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar); //JAE - Couldn't find this method to execute it

        // Set up action bar
        /*
        DrawerLayout drawer = (DrawerLayout) localView.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Set up navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */

        // Set up current state
        currentUrl = "http://192.168.1.1:8081/";
        currentClient = new UbusClient(currentUrl);

        /*
        Observable
                .fromCallable(
                        new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return currentClient.update();
                            }
                        }
                )
                .retryWhen(
                        new Func1<Observable<? extends Throwable>, Observable<?>>() {
                            @Override
                            public Observable<?> call(Observable<? extends Throwable> observable) {
                                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                                    @Override
                                    public Observable<?> call(Throwable throwable) {
                                        if (throwable instanceof UbusRpcException)
                                            return Observable.timer(5, TimeUnit.SECONDS);
                                        return Observable.empty();
                                    }
                                });
                            }
                        }
                )
                .subscribeOn(Schedulers.io())
                .subscribe();
        */

        //JAE navigationView.setCheckedItem(R.id.nav_explore);
        this.setExploreFragment();
    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    */

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_explore) {
            this.setExploreFragment();
        } else if (id == R.id.nav_custom) {
            this.setCustomFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    */

    public void setCustomFragment() {
        //this.setTitle(R.string.custom_name);
        //getSupportFragmentManager().beginTransaction()
        getFragmentManager().beginTransaction()
                .replace(R.id.main_content, new CustomCallFragment())
                .commit();
    }

    public void setExploreFragment() {
        //this.setTitle(R.string.explore_name);
        //getSupportFragmentManager().beginTransaction()
        getFragmentManager().beginTransaction()
                .replace(R.id.main_content, new ObjectExploreFragment())
                .commit();
    }

    public Object makeUbusRpcClientCall(String ubusObject, String ubusMethod, Map arguments)
            throws UbusRpcException {
        UbusRpcClient rpcClient = new UbusRpcClient(currentUrl);
        return rpcClient.call(ubusObject, ubusMethod, arguments);
    }

    @Override
    public Object makeUbusClientCall(String ubusObject, String ubusMethod, Map arguments) throws UbusRpcException {
        return currentClient.call(ubusObject, ubusMethod, arguments);
    }

    @Override
    public Observer getCallResultObserver() {
        return new Observer() {
            @Override
            public void update(Observable observable, Object o) {

            }
        };
    }
        /*
        return new Observer<Object>() {
            public void update(Observable localOber, Object localObj) {

            }

            public void onCompleted() {
                return void;
            }

            public void onError(Throwable e) {

            }

            public void onNext(Object o) {
                if (o == null) {
                    Log.d(TAG, "Doing nothing, result is null");
                }
                Gson gson = new Gson();
                try {
                    Log.d(TAG, "Received value " + gson.toJson(o));
                } catch (JsonParseException e) {
                    Log.wtf(TAG, "Failed json serialization of object", e);
                }

            }
       };
    }
    */

    @Override
    public void handleCallError(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void switchToMethodExploreFragment(String object) {
        Log.d(TAG, "Switching to new fragment (dry)");
    }
}

