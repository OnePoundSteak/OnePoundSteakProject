package honda.onepoundsteakproject;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class ViewActivityFragment extends Fragment {

    public ViewActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
