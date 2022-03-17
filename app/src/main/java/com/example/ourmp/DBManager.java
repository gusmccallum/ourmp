package com.example.ourmp;



import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Subscribed;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DBManager {

    String userID = "";

    public void setUserID(String ID) {
        userID = ID;
    }



    // Callback instance
    subObjCallback subObjCallbackInstance;

    // Callback definition
    public interface subObjCallback {
        void getSub(Subscribed cbReturnSub);
    }

    // Callback setter
    public void setSubObjCallbackInstance(subObjCallback main) {
        subObjCallbackInstance = main;
    }


    // Create Methods

    public void addNewUserSubscription() {
        Subscribed item = Subscribed.builder()
                .userId(userID)
                .build();
        Amplify.DataStore.save(
                item,
                success -> {
                    Log.i("Amplify", "Added new user to Subscribed: " + success.item().getId());
                },
                error -> {
                    Log.e("Amplify", "Could not save user to DataStore", error);
                }
        );
    }

    // Read Methods

    public void getSubscriptionObject() {
        Amplify.DataStore.query(Subscribed.class, Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        Log.i("Amplify", "Id: " + item.getId() + " - User ID: " + item.getUserId());
                        subObjCallbackInstance.getSub(item);
                    }
                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
    }

    // Update Methods

    public void addMPSubscription(String MPName) {
        Amplify.DataStore.query(Subscribed.class, Where.matches(Subscribed.USER_ID.eq(userID)),
                //query subscribed object matching with userID
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        //found matched object with the userid
                        List<String> MPNames;
                        if (item.getSubscribedMPs() != null) {
                            MPNames = item.getSubscribedMPs();
                        }
                        else {
                            MPNames = new ArrayList<String>();
                            MPNames.add(MPName);
                        }
                        Subscribed updatedItem = item.copyOfBuilder()
                                .subscribedMPs(MPNames)
                                .build();
                        //created updated subscribed object with new MP name
                        //save the data
                        Amplify.DataStore.save(
                                updatedItem,
                                success -> {
                                    Log.i("Amplify", "Item updated: " + success.item().getId());
                                    subObjCallbackInstance.getSub(item);
                                },
                                error -> {
                                    Log.e("Amplify", "Could not save item to DataStore", error);
                                }
                        );
                    }
                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
    }

    public void addBillSubscription(String BillID) {
        Amplify.DataStore.query(Subscribed.class, Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        List<String> Bills = item.getSubscribedBills();
                        Bills.add(BillID);
                        Subscribed updatedItem = item.copyOfBuilder()
                                .subscribedMPs(Bills)
                                .build();

                        Amplify.DataStore.save(
                                updatedItem,
                                success -> {
                                    Log.i("Amplify", "Item updated: " + success.item().getId());
                                    subObjCallbackInstance.getSub(item);
                                },
                                error -> {
                                    Log.e("Amplify", "Could not save item to DataStore", error);
                                }
                        );
                    }
                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );

    }

    // Delete methods

    public void deleteUser() {
        Amplify.DataStore.query(Subscribed.class, Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed toDeleteItem = items.next();
                        Amplify.DataStore.delete(toDeleteItem,
                                deleted -> {
                                    Log.i("Amplify", "Deleted item.");
                                },
                                failure -> {
                                    Log.e("Amplify", "Delete failed.", failure);
                                }
                        );
                    }
                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
    }

    public void removeMPSubscription(String MPName) {
        Amplify.DataStore.query(Subscribed.class, Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    //found matched userid
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        List<String> MPNames = item.getSubscribedMPs();
                        int rmIndex = MPNames.indexOf(MPName);

                        if (rmIndex != -1) {
                            MPNames.remove(rmIndex);

                            Subscribed updatedItem = item.copyOfBuilder()
                                    .subscribedMPs(MPNames)
                                    //added @nullable in the method so that MPNames can be 0 after removal
                                    .build();

                            Amplify.DataStore.save(
                                    updatedItem,
                                    success -> {
                                        Log.i("Amplify", "Item updated: " + success.item().getId());
                                    },
                                    error -> {
                                        Log.e("Amplify", "Could not save item to DataStore", error);
                                    }
                            );
                        } else {
                            Log.e("Amplify", "MP Name not found in subscriptions.");
                        }
                    }
                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
    }

    public void removeBillSubscription(String BillID) {
        Amplify.DataStore.query(Subscribed.class, Where.matches(Subscribed.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed item = items.next();
                        List<String> Bills = item.getSubscribedBills();
                        int rmIndex = Bills.indexOf(BillID);

                        if (rmIndex != -1) {
                            Bills.remove(rmIndex);

                            Subscribed updatedItem = item.copyOfBuilder()
                                    .subscribedBills(Bills)
                                    .build();

                            Amplify.DataStore.save(
                                    updatedItem,
                                    success -> {
                                        Log.i("Amplify", "Item updated: " + success.item().getId());
                                    },
                                    error -> {
                                        Log.e("Amplify", "Could not save item to DataStore", error);
                                    }
                            );
                        } else {
                            Log.e("Amplify", "MP Name not found in subscriptions.");
                        }
                    }
                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
    }
}

