package com.example.petmonitoruserend;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.robinhood.spark.SparkView;

//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class HeWhoHandlesAll<f> {

    public HeWhoHandlesAll() {
        // Required empty public constructor
    }

    public ArrayList<String> datain = new ArrayList<String>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BluetoothSocket socket = null;
    private Boolean cancelled;
    private BluetoothServerSocket serverSocket;
    public boolean onBluetoothPage(BluetoothManager BM, MainActivity m) throws IOException {
        BluetoothAdapter Mbt = BM.getAdapter();
        String deviceName = "Cat Thermometer";
        BluetoothDevice result = null;
        Set<BluetoothDevice> devices = Mbt.getBondedDevices();
        if (devices != null) {
            for (BluetoothDevice device : devices) {
                if (deviceName.equals(device.getName())) {
                    result = device;
                    Log.i("deviceName", result.getName());
                    break;
                }
            }
        }
        if (result != null){
            Log.d("BT", "Trying Socket...");
            UUID MY_UUID = result.getUuids()[0].getUuid();
            socket=result.createRfcommSocketToServiceRecord(MY_UUID);
            this.serverSocket = Mbt.listenUsingRfcommWithServiceRecord("test", result.getUuids()[0].getUuid()); // 1
            Log.d("BT", "Got Socket!");
        } else{
            this.socket = null;
            this.cancelled = true;
            Log.d("bluetooth", "failed bt socket");
        }

        this.cancelled = false;
        if (Mbt.isEnabled()){
        } else{
            Mbt.enable();
        }
        byte[] buf = new byte[2048];
        int bytes;
        OutputStream tmpOut = null;
        InputStream tmpIn = null;
        if(socket!=null){
            Log.d("BT", "Connecting...");
            socket.connect();
            Log.d("BT", "Connected!!"); }
        int i = 0;
        int inval = 0;
        while(true){
            boolean flag = false;
            String message = null;
                try{
                    if(i==10){
                        break;
                    }
                    tmpIn = this.socket.getInputStream();
                    inval = tmpIn.read(buf);
                    message = new String(buf, 0, inval);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(message.contains("ece445") | inval == 4){
                    return true;
                }
                else{
                    proc(message, inval, m, i);
                }
                i += 1;
        }
        return true;
    }
    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public void proc(String data, int numbytes, MainActivity m, int i){
        String[] usestring = null;
//        Log.d("NumBytes", String.valueOf(numbytes));
        String[] accvals = new String[numbytes];
        float[] tempx = new float[numbytes];
        float[] tempy = new float[numbytes];
        float[] tempz = new float[numbytes];
        float[] tempv = new float[numbytes];
        // parse data string into
        usestring = data.split("\t", 3);
        tempv[i] = Float.parseFloat(usestring[1]);
        accvals[i] = usestring[0];
        // parse accelerometer axis data
        String[] split = accvals[i].split(",", 3);
        tempx[i] = Float.parseFloat(split[0]);
        tempy[i] = Float.parseFloat(split[1]);
        tempz[i] = Float.parseFloat(split[2]);
        m.setTempvals(numbytes, tempv, i);
        int zerocnt = 0;
        m.setYlist(numbytes, tempy, i);
        for (float f: m.ylist){
            if(f==0){
                zerocnt += 1; }}
        float[] dd = new float[30];
        int j = 0;
        int k = 0;
        for (float f:m.ylist){
            if(f!=0){
                dd[j] = f;
                j += 1; }}
        m.setYlist(numbytes, dd, i);
        int n = 0;
        float[] dk = new float[30];
        m.setZlist(numbytes, tempz, i);
        for (float f:m.ylist){
            if(f!=0){
                dk[n] = f;
                n += 1; }}
        m.setZlist(numbytes, dk, i);
        float[] dy = new float[30];
        int p = 0;
        m.setXlist(numbytes, tempx, i);
        for (float f:m.ylist){
            if(f!=0){
                dy[p] = f;
                p += 1; } }
        m.setXlist(numbytes, dy, i);
    }

    public float[] tempHandler(SparkView sparkView, MainActivity m){
        float[] usearr = m.gettempvals();
        float sum = 0;
        float max_ = 0;
        float min_ = 1000;
        float[] actualarr = new float[usearr.length];
        int i = 0;
        int zerocnt = 0;
        for (float f:usearr){
            if(f!=0){
                actualarr[i] = f;
                i += 1; }
            else{
                zerocnt += 1; }
            sum += f;}
        for (float f:usearr){
            if(f>max_){
                max_ = f;}
            if(min_>f){
                min_ = f;}}
        float avg = 0;
        if(usearr.length!=0){
            avg = sum/(usearr.length-zerocnt);
        }
        if(sparkView!=null){
            sparkView.setAdapter(new MyAdapter(usearr));
        }
        float[] ret = new float[2];
        ret = new float[]{avg, max_};
        return ret;
    }

    public float[] accHandler(MainActivity m){
        float[] tempx = m.getXvals();
        float[] tempy = m.getYvals();
        float[] tempz = m.getZvals();
        float value = 0;
        float active = 0;
        float inactive = 0;
        float oldx = 0;
        float oldy = 0;
        float oldz = 0;
        float useval = 0;
        for (int i = 0; i<tempx.length; i++){
            if(Math.abs(tempx[i]-oldx)+ Math.abs(tempy[i]-oldy)+Math.abs(tempz[i]-oldz)>2){
                active += 1;
            }
            else{
                inactive += 1;
            }
            float ret = (float) Math.sqrt(Math.pow(Math.abs(tempx[i]-oldx), 2) + Math.pow(Math.abs(tempy[i]-oldy), 2) + Math.pow(Math.abs(tempz[i]-oldz), 2));
            ret = ret/9;
            useval += ret;
            value = (tempx[i] + tempy[i]);
            oldx = tempx[i];
            oldy = tempy[i];
            oldz = tempz[i];
        }
        float activity_ratio = active/(inactive);
        if(activity_ratio>0.25) activity_ratio += 0.2;
        if(activity_ratio<0.1) useval = 0;
        float[] sus = new float[]{activity_ratio, useval};
        return sus;
    }


}
