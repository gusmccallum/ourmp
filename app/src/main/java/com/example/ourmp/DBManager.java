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
        AtomicReference<Subscribed> subObject = null;
        Amplify.DataStore.query(
                Subscribed.class,
                Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        subObject.set(item);
                        Log.i("Amplify", "Id: " + item.getId() + " - User ID: " + item.getUserId());
                    }
                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
        Subscribed subObj = (Subscribed) subObject.get();
        return subObj;
    }

    public ArrayList<String> getMPSubscriptions() {
        AtomicReference<ArrayList<String>> AtomicMPs = null;
        Amplify.DataStore.query(
                Subscribed.class,
                Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        AtomicMPs.set((ArrayList<String>) item.getSubscribedMPs());
                        Log.i("Amplify", "Id: " + item.getId() + " - User ID: " + item.getUserId());
                    }
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
        ArrayList<String> MPs = (ArrayList<String>) AtomicMPs.get();
        return MPs;
    }

    public ArrayList<String> getBillSubscriptions() {
        AtomicReference<ArrayList<String>> AtomicBills = null;
        Amplify.DataStore.query(
                Subscribed.class,
                Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        AtomicBills.set((ArrayList<String>) item.getSubscribedBills());
                        Log.i("Amplify", "Id: " + item.getId() + " - User ID: " + item.getUserId());
                    }
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
        ArrayList<String> Bills = (ArrayList<String>) AtomicBills.get();
        return Bills;
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
