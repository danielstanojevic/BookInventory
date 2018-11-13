package com.example.android.products;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.products.data.ProductContract;
import com.example.android.products.data.ProductContract.ProductEntry;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {
    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        // Find the columns of product attributes that we're interested in
        int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        final int id = cursor.getInt(idColumnIndex);


        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        summaryTextView.setText(productQuantity);

        final Button sold = view.findViewById(R.id.sell_product_button);

        sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.valueOf(productQuantity);
                if (quantity > 0) {
                    quantity--;
                    ContentValues values = new ContentValues();
                    Uri uri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                    int rowsAffected = context.getContentResolver().update(uri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(context, R.string.editor_update_product_failed,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(context, R.string.editor_update_product_successful,
                                Toast.LENGTH_SHORT).show();
                    }
                    //summaryTextView.setText(String.valueOf(quantity));

                } else {
                    Toast.makeText(context,"Time to restock", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
