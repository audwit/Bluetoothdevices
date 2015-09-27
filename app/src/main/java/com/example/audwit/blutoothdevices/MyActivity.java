package com.example.audwit.blutoothdevices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MyActivity extends ActionBarActivity {

    private ListView myListView;
    private ArrayAdapter<String> BTArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_2ndscreen);
        findBluetoothDevices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    public void findBluetoothDevices()
    {
        String strbluetooth="";
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
            {
                strbluetooth = "Device does not support Bluetooth";
                Toast.makeText(getApplicationContext(), "No BT Adapter",Toast.LENGTH_LONG).show();
                return;
            }
        if(!mBluetoothAdapter.isEnabled())
            {
                int REQUEST_ENABLE_BT = 1;
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            }

        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(BluetoothDevice.ACTION_FOUND);
        ifilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        ifilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        //registerReceiver(breciever,ifilter);
        if(mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();




        myListView = (ListView) findViewById(R.id.listView);
        BTArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        myListView.setAdapter(BTArrayAdapter);
        //return strbluetooth;
        Log.d("<x_x>", "created array adapter..");
        registerReceiver(breciever, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        Log.d("<x_x>", "registered receiver..");

        BTArrayAdapter.clear();
        mBluetoothAdapter.startDiscovery();
        Toast.makeText(getApplicationContext(), "Discovery started..",Toast.LENGTH_LONG).show();

}

    private final BroadcastReceiver breciever = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("<x_x>", "inside broadcast receiver..");

            String action = intent.getAction();
//            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
//                Toast toast = new Toast.makeText(context,"Discovery Started",3);
//            toast.show();
            Toast.makeText(getApplicationContext(), "Inside receiver: " + intent.getAction(),Toast.LENGTH_SHORT).show();
            Log.d("<x_x>", "inside broadcast receiver.."+ intent.getAction());
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };
}
