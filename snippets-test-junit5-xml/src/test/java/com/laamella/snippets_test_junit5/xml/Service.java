package com.laamella.snippets_test_junit5.xml;

import generated.Itemtype;
import generated.Shiporder;
import generated.Shiptotype;
import org.tempuri.purchaseorderschema.Address;
import org.tempuri.purchaseorderschema.PurchaseOrder;

import java.math.BigDecimal;

public class Service {
    public Shiporder doWork(PurchaseOrder order) {
        Shiporder shiporder = new Shiporder();
        Itemtype itemtype = new Itemtype();
        itemtype.setTitle("Ball");
        itemtype.setPrice(BigDecimal.TEN);
        shiporder.getItems().add(itemtype);
        Shiptotype shipto = new Shiptotype();
        Address address = order.getShipTos().get(0);
        shipto.setAddress(address.getStreet());
        shipto.setCity(address.getCity());
        shipto.setName(address.getName());
        shiporder.setShipto(shipto);
        return shiporder;
    }
}
