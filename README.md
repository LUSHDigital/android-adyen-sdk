# Android Adyen SDK
## Totally unofficial

Android implementation of the Adyen payment gateway.

If you're looking for the official SDK, the repository is [here](https://github.com/Adyen/adyen-checkout-android).

### Why?

This unofficial version exists because the official SDK offered by Adyen is impractical with
it's own use of the Application singleton for something that should not be stored there anyway.
This is also inspired by the very easy to use Stripe SDK for Android.

### How to use
Simple, just instantiate the `Adyen` class with the following information:

- Context: This is so the class can maintain it's own RequestQueue with Volley
- Token: Here you can either supply your production or development token
- Use test environment: This is tied to the previous argument. If you supplied a production
token, this should be `false` and `true` if you've supplied your development token.

So, like this:

```
Adyen adyen = new Adyen(getContext(), productionToken, false);
```

Before you can encrypt customer information locally, you need to retrieve your public key.
Easy, just prefetch your key at the earliest time you can by calling:

```
adyen.fetchPublicKey(new OnPublicKeyDownloadListener()
{
    @Override
    public void onSuccess(String publicKey)
    {
        // Keep hold of publicKey, you'll need it later
    }

    @Override
    public void onFailure()
    {
        // You can't encrypt data without this, show an error message and tell user to try later
    }
});
```

Then, when you're ready to encrypt the user's card data, just create a `CardPaymentData` object with their information:

```
CardPaymentData card = new CardPaymentData(name, longCardNumber, expiryMonth, expiryYear, cvv);
```

Which is given to the Adyen class for it to generate the serialised card data, like so:
```
String cardData = adyen.serialise(card, yourPublicKey);
```

That's it! You've now got your serialised card data to give to your merchant server or however you're processing payments against the Adyen service.

Feel free to improve this however you wish.
