package tech.andrav.loftmoney;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> mItemsList = new ArrayList<>();
    private Bundle bundle;
    private ItemsAdapterListener mListener;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();


    public ItemsAdapter(Bundle bundle) {

        this.bundle = bundle;
    }

    public void setListener(final ItemsAdapterListener mListener) {

        this.mListener = mListener;
    }

    public void clearSelections() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(final int position) {
        mSelectedItems.put(position, !mSelectedItems.get(position));
        notifyDataSetChanged();
    }

    public void clearItem(final int position) {
        mSelectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        int result = 0;
        for (int i = 0; i < mItemsList.size(); i++) {
            if (mSelectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (Item item : mItemsList) {
            if (mSelectedItems.get(i)) {
                result.add(item.getId());
            }
            i++;
        }
        return result;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view, null);
        return new ItemViewHolder(itemView, bundle);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindItem(mItemsList.get(position), mSelectedItems.get(position));
        holder.setListener(mListener, mItemsList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }


    public void addItem(Item item) {
        mItemsList.add(item);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mItemsList.clear();
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;
        private TextView mPriceView;
        private View mItemView;

        public ItemViewHolder(@NonNull View itemView, Bundle bundle) {
            super(itemView);

            mItemView = itemView;
            mNameView = itemView.findViewById(R.id.name_view);
            mPriceView = itemView.findViewById(R.id.price_view);
            final Context context = mPriceView.getContext();
            mPriceView.setTextColor(ContextCompat.getColor(
                    context,
                    bundle.getInt(MainActivity.COLOR_ID)));
        }

        public void bindItem(final Item item, final boolean isSelected) {
            mItemView.setSelected(isSelected);
            mNameView.setText(item.getName());
            mPriceView.setText(
                    mPriceView.getContext().getResources().getString(R.string.price_with_currency,
                    String.valueOf(item.getPrice())));
        }

        public void setListener(final ItemsAdapterListener listener, final Item item, final int position){

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    listener.onItemClick(item, position);
                }
            });

            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    listener.onItemLongClick(item, position);
                    return false;
                }
            });
        }
    }
}
