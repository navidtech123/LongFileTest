package com.example.commentsapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class PremiumActivity extends AppCompatActivity {


    String TAG = "TestINAPP";
    private BillingClient billingClient;
    List<ProductDetails> productDetailsList = new ArrayList<>();
    Handler handler;
    ProductDetailsAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutCompat noData;
    ProgressBar loadProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase);


        initView();
        //Initialize a BillingClient with PurchasesUpdatedListener onCreate method
        handler = new Handler();

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        new PurchasesUpdatedListener() {
                            @Override
                            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                    for (Purchase purchase : list) {
                                        verifyPurchase(purchase);
                                    }
                                }
                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        connectGooglePlayBilling();


    }

    private void initView() {
        recyclerView= findViewById(R.id.recyclerView);
        noData= findViewById(R.id.noData);
        loadProducts= findViewById(R.id.loadProducts);

    }

    void connectGooglePlayBilling() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                connectGooglePlayBilling();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    showProducts();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    void showProducts() {
        Log.d(TAG, "showProducts");
        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("buy_pur_1")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),

                //Product 2
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("buy_pur_2")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),

                //Product 3
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("buy_pur_3")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),

                //Product 4
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("buy_pur_4")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, list) -> {
            Log.d(TAG, list.size() + " List size");

            productDetailsList.clear();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (list.size() > 0) {
                        noData.setVisibility(View.GONE);
                    } else {
                        noData.setVisibility(View.VISIBLE);
                    }

                    Log.d(TAG, "posted delayed");
                    loadProducts.setVisibility(View.INVISIBLE); //
                    productDetailsList.addAll(list);
                    Log.d(TAG, productDetailsList.size() + " number of products");
                    adapter = new ProductDetailsAdapter(getApplicationContext(), productDetailsList, new ProductDetailsAdapter.OnItemClick() {
                        @Override
                        public void onItemClick(int pos) {
                            launchPurchaseFlow(productDetailsList.get(pos));
                        }
                    });
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PremiumActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }, 1000);

        });
    }


    void launchPurchaseFlow(ProductDetails productDetails) {

        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                );
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        billingClient.launchBillingFlow(this, billingFlowParams);
    }


    void verifyPurchase(Purchase purchase) {


//       billingClient.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build());
        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        ConsumeResponseListener listener = (billingResult, s) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                giveUserCoins(purchase);
            }
        };
        billingClient.consumeAsync(consumeParams, listener);
    }


    protected void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                verifyPurchase(purchase);
                            }
                        }
                    }
                }
        );

    }


    @SuppressLint("SetTextI18n")
    void giveUserCoins(Purchase purchase) {

        Log.d("TestINAPP", purchase.getProducts().get(0));
        Log.d("TestINAPP", purchase.getQuantity() + " Quantity");


    }

    public static class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolderClas> {

        Context mContext;
        List<ProductDetails> productDetailsList;
        OnItemClick onItemClick;

        public ProductDetailsAdapter(Context mContext, List<ProductDetails> productDetailsList, OnItemClick onItemClick) {

            this.mContext = mContext;
            this.productDetailsList = productDetailsList;
            this.onItemClick = onItemClick;
        }

        @NonNull
        @Override
        public ViewHolderClas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
            return new ViewHolderClas(view);
        }

        @Override
        public int getItemCount() {
            return productDetailsList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderClas holder, @SuppressLint("RecyclerView") int position) {

//            holder.productItemBinding.productName.setText(productDetailsList.get(position).getName());
            holder.productName.setText("Price " + productDetailsList.get(position).getOneTimePurchaseOfferDetails().getFormattedPrice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClick(position);
                }
            });
        }

        public static class ViewHolderClas extends RecyclerView.ViewHolder {

            TextView productName;

            public ViewHolderClas(@NonNull View itemView) {
                super(itemView);
                productName = itemView.findViewById(R.id.product_name);
            }
        }


        public interface OnItemClick {
            void onItemClick(int pos);
        }

    }

}
