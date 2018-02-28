'use strict';

const functions = require('firebase-functions');
//const firestore = require('@google-cloud/firestore');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var db = admin.firestore();

// Keeps track of the length of the 'likes' child list in a separate property.
exports.countfollowersAddFirestore = functions.firestore.document('users/{userid}/followers/{followerid}').onCreate(event => {
    const collectionRef = event.data.ref.parent;
    const countRef = collectionRef.parent;
    
    return db.runTransaction(t => {
        return t.get(countRef)
            .then(doc => {
                if (doc.exists) {
                    var new_count = doc.data().follower_count + 1;
                    console.log(doc.data().follower_count);
                    t.update(countRef, { follower_count: new_count });
                }

            });
    }).then(result => {
        console.log('Transaction success!');
    })
    .catch(err => {
        console.log('Transaction failure:', err);
    });
    
});

exports.countPostsFirestore = functions.firestore.document('users/{userid}/posts/{posts}').onCreate(event => {
    const collectionRef = event.data.ref.parent;
    const countRef = collectionRef.parent;

    return db.runTransaction(t => {
        return t.get(countRef)
            .then(doc => {
                if (doc.exists) {
                    var new_count = doc.data().num_posts + 1;
                    console.log(doc.data().num_posts);
                    t.update(countRef, { num_posts: new_count });
                }

            });
    }).then(result => {
        console.log('Transaction success!');
    })
        .catch(err => {
            console.log('Transaction failure:', err);
        });

});

exports.countPostsDeleteFirestore = functions.firestore.document('users/{userid}/posts/{posts}').onDelete(event => {
    const collectionRef = event.data.ref.parent;
    const countRef = collectionRef.parent;

    return db.runTransaction(t => {
        return t.get(countRef)
            .then(doc => {
                if (doc.exists) {
                    var new_count = doc.data().num_posts - 1;
                    console.log(doc.data().num_posts);
                    t.update(countRef, { num_posts: new_count });
                }

            });
    }).then(result => {
        console.log('Transaction success!');
    })
        .catch(err => {
            console.log('Transaction failure:', err);
        });

});

exports.countfollowersDeleteFirestore = functions.firestore.document('users/{userid}/followers/{followerid}').onDelete(event => {
    const collectionRef = event.data.ref.parent;
    const countRef = collectionRef.parent;

    return db.runTransaction(t => {
        return t.get(countRef)
            .then(doc => {

                if (doc.exists) {
                    var new_count = doc.data().follower_count - 1;
                    console.log(doc.data().follower_count);
                    t.update(countRef, { follower_count: new_count });
                }

            });
    }).then(result => {
        console.log('Transaction success!');
    })
        .catch(err => {
            console.log('Transaction failure:', err);
        });

});



exports.countfollowingAddFirestore = functions.firestore.document('users/{userid}/following/{followingid}').onCreate(event => {
    const collectionRef = event.data.ref.parent;
    const countRef = collectionRef.parent;

    return db.runTransaction(t => {
        return t.get(countRef)
            .then(doc => {

                if (doc.exists) {
                    var new_count = doc.data().following_count + 1;
                    console.log(doc.data().following_count);
                    t.update(countRef, { following_count: new_count });
                }


            });
    }).then(result => {
        console.log('Transaction success!');
    })
        .catch(err => {
            console.log('Transaction failure:', err);
        });

});

exports.countfollowingDeleteFirestore = functions.firestore.document('users/{userid}/following/{followingid}').onDelete(event => {
    const collectionRef = event.data.ref.parent;
    const countRef = collectionRef.parent;

    return db.runTransaction(t => {
        return t.get(countRef)
            .then(doc => {

                if (doc.exists) {
                    var new_count = doc.data().following_count - 1;
                    console.log(doc.data().following_count);
                    t.update(countRef, { following_count: new_count });
                }


            });
    }).then(result => {
        console.log('Transaction success!');
    })
        .catch(err => {
            console.log('Transaction failure:', err);
        });

});


exports.countfollowerschange = functions.database.ref('/users/{userid}/followers/{followerid}').onWrite(event => {
    const collectionRef = event.data.ref.parent;
    const countRef = collectionRef.parent.child('follower_count');

    // Return the promise from countRef.transaction() so our function 
    // waits for this async event to complete before it exits.
    return countRef.transaction(current => {
        if (event.data.exists() && !event.data.previous.exists()) {
            return (current || 0) + 1;
        }
        else if (!event.data.exists() && event.data.previous.exists()) {
            return (current || 0) - 1;
        }
    }).then(() => {
        console.log('Counter updated.');
    });
});

// If the number of likes gets deleted, recount the number of likes
exports.recountfollowers = functions.database.ref('/user/{userid}/follower_count').onWrite(event => {
    if (!event.data.exists()) {
        const counterRef = event.data.ref;
        const collectionRef = counterRef.parent.child('followers');

        // Return the promise from counterRef.set() so our function 
        // waits for this async event to complete before it exits.
        return collectionRef.once('value')
            .then(messagesData => counterRef.set(messagesData.numChildren()));
    }
});

exports.countfollowingchange = functions.database.ref('/users/{userid}/following/{followingid}').onWrite(event => {
    const collectionRef = event.data.ref.parent;
    const countRef = collectionRef.parent.child('following_count');

    // Return the promise from countRef.transaction() so our function 
    // waits for this async event to complete before it exits.
    return countRef.transaction(current => {
        if (event.data.exists() && !event.data.previous.exists()) {
            return (current || 0) + 1;
        }
        else if (!event.data.exists() && event.data.previous.exists()) {
            return (current || 0) - 1;
        }
    }).then(() => {
        console.log('Counter updated.');
    });
});

// If the number of likes gets deleted, recount the number of likes
exports.recountfollowing = functions.database.ref('/user/{userid}/following_count').onWrite(event => {
    if (!event.data.exists()) {
        const counterRef = event.data.ref;
        const collectionRef = counterRef.parent.child('following');

        // Return the promise from counterRef.set() so our function 
        // waits for this async event to complete before it exits.
        return collectionRef.once('value')
            .then(messagesData => counterRef.set(messagesData.numChildren()));
    }
});



