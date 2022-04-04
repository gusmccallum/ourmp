package com.example.ourmp;



import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Subscribed2;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    String userID = "";

    public void setUserID(String ID) {
        userID = ID;
    }


    // Callback instance
    subObjCallback subObjCallbackInstance;

    // Callback definition
    public interface subObjCallback {
        void getSub(Subscribed2 cbReturnSub);
    }

    // Callback setter
    public void setSubObjCallbackInstance(subObjCallback main) {
        subObjCallbackInstance = main;
    }


    // Create Methods

    public void addNewUserSubscription() {
        Subscribed2 item = Subscribed2.builder()
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
        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed2 item = items.next();
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

    public void changeSubscribedUserId() {


        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                //query subscribed object matching with userID
                items -> {

                        Subscribed2 item = items.next();

                        Subscribed2 updatedItem = item.copyOfBuilder()
                                .userId("123456789")
                                .build();
                        //created updated subscribed object with new MP name
                        //save the data
                        Amplify.DataStore.save(
                                updatedItem,
                                success -> {
                                    Log.i("Amplify", "Item updated: " + success.item().getId());
                                },
                                error -> {
                                    Log.e("Amplify", "Could not save item to DataStore", error);
                                }
                        );

                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
        userID = "123456789";
    }

    public void changeSubscribedUserId2() {


        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                //query subscribed object matching with userID
                items -> {

                    Subscribed2 item = items.next();

                    Subscribed2 updatedItem = item.copyOfBuilder()
                            .userId("987654321")
                            .build();
                    //created updated subscribed object with new MP name
                    //save the data
                    Amplify.DataStore.save(
                            updatedItem,
                            success -> {
                                Log.i("Amplify", "Item updated: " + success.item().getId());
                            },
                            error -> {
                                Log.e("Amplify", "Could not save item to DataStore", error);
                            }
                    );

                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
        userID = "987654321";
    }

    public void addMPSubscription(String MPName) {
        /*Amplify.DataStore.stop(
                () -> Log.i("OurMP", "DataStore stopped"),
                error -> Log.e("OurMP", "Error stopping DataStore", error)
        );
        Amplify.DataStore.start(
                () -> Log.i("OurMP", "DataStore started"),
                error -> Log.e("OurMP", "Error starting DataStore", error)
        );*/
        /*Amplify.DataStore.query(Subscribed.class, Where.matches(Subscribed.USER_ID.eq(userID)),
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
        );*/
        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                //query subscribed object matching with userID
                items -> {
                        Subscribed2 item = items.next();
                        //found matched object with the userid
                        List<String> MPNames;
                        if (item.getSubscribedMPs() != null) {
                            MPNames = item.getSubscribedMPs();
                        }
                        else {
                            MPNames = new ArrayList<String>();
                            MPNames.add(MPName);
                        }
                        Subscribed2 updatedItem = item.copyOfBuilder()
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

                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
    }

    public void addBillSubscription(String BillID) {
        /*Amplify.DataStore.stop(
                () -> Log.i("OurMP", "DataStore stopped"),
                error -> Log.e("OurMP", "Error stopping DataStore", error)
        );
        Amplify.DataStore.start(
                () -> Log.i("OurMP", "DataStore started"),
                error -> Log.e("OurMP", "Error starting DataStore", error)
        );*/

        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed2 item = items.next();
                        List<String> Bills = item.getSubscribedBills();
                        Bills.add(BillID);
                        Subscribed2 updatedItem = item.copyOfBuilder()
                                .subscribedBills(Bills)
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
        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed2 toDeleteItem = items.next();
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
        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                items -> {
                    //found matched userid
                        Subscribed2 item = items.next();
                        List<String> MPNames = item.getSubscribedMPs();
                        int rmIndex = MPNames.indexOf(MPName);

                        if (rmIndex != -1) {
                            MPNames.remove(rmIndex);
                            if (MPNames.size() == 0) {
                                MPNames = null;
                            }

                            Subscribed2 updatedItem = item.copyOfBuilder()
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

                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
    }

    public void removeUserId() {
        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                items -> {
                    //found matched userid
                        Subscribed2 item = items.next();

                            Subscribed2 updatedItem = item.copyOfBuilder()
                                    .userId("")
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


                },
                failure -> {
                    Log.e("Amplify", "Could not query DataStore", failure);
                }
        );
    }

    public void removeBillSubscription(String BillID) {
        /*Amplify.DataStore.stop(
                () -> Log.i("OurMP", "DataStore stopped"),
                error -> Log.e("OurMP", "Error stopping DataStore", error)
        );
        Amplify.DataStore.start(
                () -> Log.i("OurMP", "DataStore started"),
                error -> Log.e("OurMP", "Error starting DataStore", error)
        );*/
        Amplify.DataStore.query(Subscribed2.class, Where.matches(Subscribed2.USER_ID.eq(userID)),
                items -> {
                    while (items.hasNext()) {
                        Subscribed2 item = items.next();
                        List<String> Bills = item.getSubscribedBills();
                        int rmIndex = Bills.indexOf(BillID);

                        if (rmIndex != -1) {
                            Bills.set(rmIndex, "");

                            Subscribed2 updatedItem = item.copyOfBuilder()
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

