package christinayyma.cloud_data_transfer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<List<Integer>> batch;
    private int num_rows;
    priavte int num_sensors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num_rows = 22;
        num_sensors = 22;
        batch = new ArrayList<List<Integer>>();

        for(int i = 0; i < num_rows; i++) {
            List<Integer> row = new ArrayList<Integer>();
            for (int j = 0; j < num_sensors; j++) {
                row.add(j);
            }
            batch.add(row);
        }



        setContentView(R.layout.activity_main);
    }
}
