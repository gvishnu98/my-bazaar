package com.example.mybazaar;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition=-1;

    private TextView cartTotalAmount;
    private boolean showDeleteBtn;
    public CartAdapter(List<CartItemModel> cartItemModelList,TextView  cartTotalAmount,boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount=cartTotalAmount;
        this.showDeleteBtn=showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
       switch (cartItemModelList.get(position).getType()){
           case 0:
               return CartItemModel.CART_ITEM;
           case 1:
               return CartItemModel.TOTAL_AMOUNT;
           default:
               return -1;
       }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
                return new CartItemViewHolder(cartItemView);
                case CartItemModel.TOTAL_AMOUNT:
                    View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout,viewGroup,false);
                    return new cartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
          switch (cartItemModelList.get(position).getType()){
              case CartItemModel.CART_ITEM:
                  String productID=cartItemModelList.get(position).getProductID();
                  String resource=cartItemModelList.get(position).getProductImage();
                  String title=cartItemModelList.get(position).getProductTitle();
                  String productPrice=cartItemModelList.get(position).getProductPrice();
                  String cutPrice=cartItemModelList.get(position).getCutPrice();
                  Long offersApplied=cartItemModelList.get(position).getOffersApplied();

                  ((CartItemViewHolder)viewHolder).setItemDetails(productID,resource,title,productPrice,cutPrice,offersApplied,position);
                  break;
                  case CartItemModel.TOTAL_AMOUNT:
                      int totalItems=0;
                      int totalItemPrice=0;
                      String deliveryPrice;
                      int totalAmount;
                      int savedAmount=0;
                      for (int x=0;x<cartItemModelList.size();x++){
                          if (cartItemModelList.get(x).getType() ==CartItemModel.CART_ITEM){
                              totalItems++;
                              totalItemPrice=totalItemPrice+Integer.parseInt(cartItemModelList.get(x).getProductPrice());
                          }
                      }
                      if (totalItemPrice>500){
                          deliveryPrice="FREE";
                          totalAmount=totalItemPrice;
                      }else{
                          deliveryPrice="40";
                          totalAmount=totalItemPrice+40;
                      }


                      ((cartTotalAmountViewholder)viewHolder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount);
                      break;
              default:
                  return;
          }

          if (lastPosition<position){
              Animation animation= AnimationUtils.loadAnimation(viewHolder.itemView.getContext(),R.anim.fade_in);
              viewHolder.itemView.setAnimation(animation);
              lastPosition=position;
          }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView productPrice;
        private TextView cutPrice;
        private TextView offersApplied;
        private TextView productQuantity;

        private LinearLayout deleteBtn;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image);
            productTitle=itemView.findViewById(R.id.product_title);
            productPrice=itemView.findViewById(R.id.product_price);
            cutPrice=itemView.findViewById(R.id.cut_price);
            offersApplied=itemView.findViewById(R.id.offers_applied);
            productQuantity=itemView.findViewById(R.id.product_quantity);

            deleteBtn=itemView.findViewById(R.id.remove_item_btn);
        }
        private void setItemDetails(String productID, String resource, String title, String productPriceText, String cutPriceText, Long offersAppliedNo, final int position){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.animation)).into(productImage);
            productTitle.setText(title);
            productPrice.setText("Rs."+productPriceText+"/-");
            cutPrice.setText("Rs."+cutPriceText+"/-");
            if(offersAppliedNo>0){
                offersApplied.setVisibility(View.VISIBLE);
                offersApplied.setText(offersAppliedNo + "Offers applied");
            }else {
                offersApplied.setVisibility(View.INVISIBLE);
            }
            productQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog quantityDialog=new Dialog(itemView.getContext());
                    quantityDialog.setContentView(R.layout.quantity_dialog);
                    quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    quantityDialog.setCancelable(false);
                    final EditText quantityNo=quantityDialog.findViewById(R.id.quantity_no);
                    Button cancelBtn=quantityDialog.findViewById(R.id.cancel_btn);
                    Button okBtn=quantityDialog.findViewById(R.id.ok_btn);

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            quantityDialog.dismiss();
                        }
                    });

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            productQuantity.setText("Qty: " + quantityNo.getText());
                            quantityDialog.dismiss();
                        }
                    });
                    quantityDialog.show();
                }
            });
            if (showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }else{
                deleteBtn.setVisibility(View.INVISIBLE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ProductDetailsActivity.running_cart_query){
                        ProductDetailsActivity.running_cart_query=true;

                        DBqueries.removeFromCart(position,itemView.getContext());
                    }
                }
            });
        }
    }

    class cartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public cartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);

            totalItems=itemView.findViewById(R.id.total_items);
            totalItemPrice=itemView.findViewById(R.id.total_items_price);
            deliveryPrice=itemView.findViewById(R.id.delivery_price);
            totalAmount=itemView.findViewById(R.id.total_price);
            savedAmount=itemView.findViewById(R.id.saved_amount);
        }
        private void setTotalAmount(int totalItemText,int totalItemPriceText,String deliveryPriceText,int totalAmountText,int savedAmountText){
            totalItems.setText("Price("+totalItemText+"items)");
            totalItemPrice.setText("Rs."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("FREE")){
                deliveryPrice.setText(deliveryPriceText);
            }else{
                deliveryPrice.setText("Rs."+deliveryPriceText+"/-");
            }
            totalAmount.setText("Rs."+totalAmountText+"/-");
            cartTotalAmount.setText("Rs."+totalAmountText+"/-");
            savedAmount.setText("You saved Rs."+savedAmountText+"/- on this order");
        }
    }
}
