package com.vaquilar.finalreq.mshopping.helper;

import com.vaquilar.finalreq.mshopping.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class Data {

    List<Offer> offerList = new ArrayList<>();


    public List<Offer> getOfferList() {
        Offer offer = new Offer("https://image.freepik.com/free-psd/grocery-sale-delivery-special-promo-food-product-menu-discount-promotion-social-media-instagram-story-banner-template_173494-44.jpg");
        offerList.add(offer);
        offer = new Offer("https://www.coredna.com/web_images/blogs/71/961/ecommerce-promotion-strategies-discounts-coupons.png");
        offerList.add(offer);
        offer = new Offer("https://cdn1.clickthecity.com/wp-content/uploads/2020/07/03151243/Fruits-and-Vegetables-Delivery-768x432.png");
        offerList.add(offer);
        offer = new Offer("https://www.franchisemarket.ph/application/files/7615/8006/0555/milk-tea-philippines.jpg");
        offerList.add(offer);
        offer = new Offer("https://s3.theasianparent.com/cdn-cgi/image/width=450,quality=90/tap-assets-prod/wp-content/uploads/sites/11/2020/03/grocery-delivery-philippines-jannoon028.jpg");
        offerList.add(offer);
        offer = new Offer("https://www.sbs.com.au/food/sites/sbs.com.au.food/files/styles/full/public/images/5854_4-pinakbet.jpg?itok=qKqXd1dt");
        offerList.add(offer);


        return offerList;
    }
}
