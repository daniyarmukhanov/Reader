package artlines.kz.reader;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;


public class MainActivity extends ListActivity {
    private LayoutInflater inflater;
    private List<RowData> contentDetails;
    Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EpubReader epubReader = new EpubReader();
        contentDetails = new ArrayList<RowData>();
        TextView title=(TextView)findViewById(R.id.title1);

        AssetManager assManager = getApplicationContext().getAssets();
        InputStream is = null;
        try {
            is = assManager.open("shapagat.epub");
            book=epubReader.readEpub(is, "UTF-8");
            logContentsTable(book.getTableOfContents().getTocReferences(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        title.setText(book.getTitle());

        inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        CustomAdapter adapter = new CustomAdapter(this, R.layout.list,
                R.id.title, contentDetails);
        setListAdapter(adapter);
        getListView().setTextFilterEnabled(true );


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
//    private class RowData{
//        private String title;
//        private Resource resource;
//
//        public RowData() {
//            super();
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public Resource getResource() {
//            return resource;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public void setResource(Resource resource) {
//            this.resource = resource;
//        }
//
//    }
    private class CustomAdapter extends ArrayAdapter<RowData> {

        public CustomAdapter(Context context, int resource,
                             int textViewResourceId, List<RowData> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        private class ViewHolder{
            private View row;
            private TextView titleHolder = null;

            public ViewHolder(View row) {
                super();
                this.row = row;
            }

            public TextView getTitle() {
                if(null == titleHolder)
                    titleHolder = (TextView) row.findViewById(R.id.title);
                return titleHolder;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            TextView title = null;
            RowData rowData = getItem(position);
            if(null == convertView){
                convertView = inflater.inflate(R.layout.list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            title = holder.getTitle();
            title.setText(rowData.getTitle());
            return convertView;
        }

    }

    private void logContentsTable(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            return;
        }
        for (TOCReference tocReference:tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            tocString.append(tocReference.getTitle());
            RowData row = new RowData();
            row.setTitle(tocString.toString());
            row.setResource(tocReference.getResource());
            contentDetails.add(row);
            logContentsTable(tocReference.getChildren(), depth + 1);
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        RowData rowData = contentDetails.get(position);
        Intent intent = new Intent(MainActivity.this, ContentView.class);
        Globals.rowData=rowData;
        startActivity(intent);

    }
}
