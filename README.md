[![Build Status](https://travis-ci.org/westonal/blockchain.svg?branch=master)](https://travis-ci.org/westonal/blockchain)

Tech Stack
==

- 100% Kotlin
- Databinding
- Model View Intent
- [Retrofit 2](http://square.github.io/retrofit/)
- [RxJava 2](https://github.com/ReactiveX/RxJava)
- [Kodein DI](http://kodein.org/)
- [Kluent](https://github.com/MarkusAmshove/Kluent)
- [Timber](https://github.com/JakeWharton/timber)
- [Travis](https://travis-ci.org/westonal/blockchain)

Features
==

- High emphasis on quality and testing, with Travis integration from the beginning with master branch requiring tests to pass

- Fully test-driven MVI "dialog" applies 2 intent types, `NewXpubIntent` and `RefreshIntent`, to get an `Observable` stream of View Models

- The Main activity just subscribes to the VM stream

- Error handling, such as no-network, appears as a card, with tap to retry

- Pull down to refresh

Future work
==

- A new request is fired on each activity resume, VMs are cheap to make, so caching at OkHttp client level is an option that both solves this and prevents an app calling the server too frequently. Other options are to cache the VM in the RX stream.

- Use `DiffUtil` to animate only the changes in the list in the `RecyclerView`.

- Technically the transactions are homogeneous content and so shouldn't have their own cards.
