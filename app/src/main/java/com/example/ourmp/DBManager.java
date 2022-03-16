package com.example.ourmp;


import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Subscribed;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DBManager {

    String userID = "1234abcd";

    // Query variables
    Subscribed returnedSub = null;
    ArrayList<String> MPSubs = new ArrayList<String>();
    ArrayList<String> BillSubs = new ArrayList<String>();


    // Create Methods

    public boolean addNewUserSubscription(ArrayList<String> MPs, ArrayList<String> Bills) {
        AtomicBoolean atomicResult = new AtomicBoolean(false);
        Subscribed item = Subscribed.builder()
                .userId(userID)
                .subscribedMPs(MPs)
                .subscribedBills(Bills)
                .build();
        Amplify.DataStore.save(
                item,
                success -> {
                    atomicResult.set(true);
                    Log.i("Amplify", "Saved item: " + success.item().getId());
                },
                error -> {
                    atomicResult.set(false);
                    Log.e("Amplify", "Could not save item to DataStore", error);
                }
        );
        boolean result = (boolean) atomicResult.get();
        return result;
    }

    // Read Methods

    public Subscribed getSubscriptionObject() {
        returnedSub = null;
        Amplify.DataStore.query(
                Subscribed.class,
                Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        Log.i("Amplify", "Id: " + item.getId() + " - User ID: " + item.getUserId());
                        returnedSub = item;
                    }
                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
        return returnedSub;
    }

    public ArrayList<String> getMPSubscriptions() {
        MPSubs.clear();
        Amplify.DataStore.query(
                Subscribed.class,
                Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        MPSubs = (ArrayList<String>) item.getSubscribedMPs();
                        Log.i("Amplify", "Id: " + item.getId() + " - User ID: " + item.getUserId());
                    }
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
        return MPSubs;
    }

    public ArrayList<String> getBillSubscriptions() {
        BillSubs.clear();
        Amplify.DataStore.query(
                Subscribed.class,
                Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        BillSubs = (ArrayList<String>) item.getSubscribedBills();
                        Log.i("Amplify", "Id: " + item.getId() + " - User ID: " + item.getUserId());
                    }
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
        return BillSubs;
    }

    // Update Methods

    public boolean addMPSubscription(String MPName) {
        AtomicBoolean atomicResult = new AtomicBoolean(false);

        Subscribed subObj = getSubscriptionObject();

        ArrayList<String> MPNames = getMPSubscriptions();
        MPNames.add(MPName);

        Subscribed updatedItem = subObj.copyOfBuilder()
                .subscribedMPs(MPNames)
                .build();

        Amplify.DataStore.save(
                updatedItem,
                success -> {
                    atomicResult.set(true);
                    Log.i("Amplify", "Item updated: " + success.item().getId());
                },
                error -> {
                    atomicResult.set(false);
                    Log.e("Amplify", "Could not save item to DataStore", error);
                }
        );

        boolean result = (boolean) atomicResult.get();
        return result;
    }

    public boolean addBillSubscription(String BillID) {
        AtomicBoolean atomicResult = new AtomicBoolean(false);

        Subscribed subObj = getSubscriptionObject();

        ArrayList<String> Bills = getBillSubscriptions();
        Bills.add(BillID);

        Subscribed updatedItem = subObj.copyOfBuilder()
                .subscribedMPs(Bills)
                .build();

        Amplify.DataStore.save(
                updatedItem,
                success -> {
                    atomicResult.set(true);
                    Log.i("Amplify", "Item updated: " + success.item().getId());
                },
                error -> {
                    atomicResult.set(false);
                    Log.e("Amplify", "Could not save item to DataStore", error);
                }
        );

        boolean result = (boolean) atomicResult.get();
        return result;
    }

    // Delete methods

    public boolean deleteUser() {
        AtomicBoolean atomicResult = new AtomicBoolean(false);

        Subscribed toDeleteItem = getSubscriptionObject();

        Amplify.DataStore.delete(toDeleteItem,
                deleted -> {
                    atomicResult.set(true);
                    Log.i("Amplify", "Deleted item.");
                },
                failure -> {
                    atomicResult.set(false);
                    Log.e("Amplify", "Delete failed.", failure);
                }
        );

        boolean result = (boolean) atomicResult.get();
        return result;
    }

    public boolean removeMPSubscription(String MPName) {
        AtomicBoolean atomicResult = new AtomicBoolean(false);

        Subscribed subObj = getSubscriptionObject();

        ArrayList<String> MPs = getMPSubscriptions();

        int rmIndex = MPs.indexOf(MPName);

        if (rmIndex != -1) {
            MPs.remove(rmIndex);

            Subscribed updatedItem = subObj.copyOfBuilder()
                    .subscribedMPs(MPs)
                    .build();

            Amplify.DataStore.save(
                    updatedItem,
                    success -> {
                        atomicResult.set(true);
                        Log.i("Amplify", "Item updated: " + success.item().getId());
                    },
                    error -> {
                        atomicResult.set(false);
                        Log.e("Amplify", "Could not save item to DataStore", error);
                    }
            );
        }
        else {
            atomicResult.set(false);
            Log.e("Amplify", "MP Name not found in subscriptions.");
        }
        boolean result = (boolean) atomicResult.get();
        return result;
    }

    public boolean removeBillSubscription(String BillID) {
        AtomicBoolean atomicResult = new AtomicBoolean(false);

        Subscribed subObj = getSubscriptionObject();

        ArrayList<String> Bills = getBillSubscriptions();

        int rmIndex = Bills.indexOf(BillID);

        if (rmIndex != -1) {
            Bills.remove(rmIndex);

            Subscribed updatedItem = subObj.copyOfBuilder()
                    .subscribedBills(Bills)
                    .build();

            Amplify.DataStore.save(
                    updatedItem,
                    success -> {
                        atomicResult.set(true);
                        Log.i("Amplify", "Item updated: " + success.item().getId());
                    },
                    error -> {
                        atomicResult.set(false);
                        Log.e("Amplify", "Could not save item to DataStore", error);
                    }
            );
        }
        else {
            atomicResult.set(false);
            Log.e("Amplify", "Bill ID not found in subscriptions.");
        }
        boolean result = (boolean) atomicResult.get();
        return result;
    }
}
