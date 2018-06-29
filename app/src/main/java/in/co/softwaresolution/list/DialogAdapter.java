package in.co.softwaresolution.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogAdapter extends ArrayAdapter {

    LayoutInflater layoutInflater;
    ArrayList<Info> arrayList;

    public DialogAdapter(@NonNull Context context, ArrayList<Info> arrayList) {
        super(context, 0, arrayList);
        this.arrayList=arrayList;

        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View output=layoutInflater.inflate(R.layout.my_layout,parent,false);

        TextView title=output.findViewById(R.id.addViewID);
        Info info=arrayList.get(position);
        title.setText(info.getTitle());

        return output;
    }
}
