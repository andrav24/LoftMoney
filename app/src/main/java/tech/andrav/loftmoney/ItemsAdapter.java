package tech.andrav.loftmoney;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> mItemsList = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view, null);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindItem(mItemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }


    public void addItem(Item item) {
        mItemsList.add(item);
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;
        private TextView mPriceView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.name_view);
            mPriceView = itemView.findViewById(R.id.price_view);
        }

        public void bindItem(final Item item) {
            mNameView.setText(item.getName());
            mPriceView.setText(
                    mPriceView.getContext().getResources().getString(R.string.price_with_currency,
                    String.valueOf(item.getPrice())));
        }
    }
}
