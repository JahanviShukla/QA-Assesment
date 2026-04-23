# WatchStudio.in - Selector Reference Guide

## 🔍 Login Button Selectors

### **Element Found:**
```html
<a class="elementor-icon" href="https://watchstudio.in/my-account/">
    <i aria-hidden="true" class="far fa-user"></i>
</a>
```

### **CSS Selectors (Recommended)**
```css
/* Most specific */
a.elementor-icon[href*='my-account']

/* By href */
a[href*='my-account']

/* By class */
a.elementor-icon

/* By icon */
i.far.fa-user
```

### **XPath Selectors**
```xpath
/* By href attribute */
//a[contains(@href, 'my-account')]

/* By class */
//a[@class='elementor-icon']

/* With icon check */
//a[@class='elementor-icon']//i[@class='far fa-user']

/* Most specific XPath */
//a[contains(@href, 'my-account')][@class='elementor-icon']
```

### **Playwright Java Code**
```java
// CSS Selector (Recommended)
Locator loginBtn = page.locator("a.elementor-icon[href*='my-account']");
loginBtn.click();

// XPath alternative
Locator loginBtnXPath = page.locator("//a[contains(@href, 'my-account')]");
loginBtnXPath.click();

// By text (if there's text)
Locator loginByText = page.getByText("Login");
```

---

## 🛒 Cart Button Selectors

### **Common WatchStudio Cart Selectors**
```css
/* Main cart button */
#hfe-menu-cart__toggle_button
a.elementor-icon[href*='/cart']
.ast-cart-menu-wrap

/* Cart icon */
.hfe-cart-icon
.cart-icon

/* Cart count badge */
.hfe-cart-count
.cart-count
.astra-cart-digits
```

### **XPath for Cart**
```xpath
//a[contains(@href, 'cart')]
//button[@id='hfe-menu-cart__toggle_button']
//div[contains(@class, 'cart-wrap')]
```

---

## 🔍 Search Button Selectors

### **Search Icon/Button**
```css
.search-trigger
.search-icon
[aria-label*='search']
.ajax-search-toggle
```

### **Search Input Field**
```css
input[type='search']
.search-input
[name='s']
#search-products-form-input
.ajax-search-input
```

### **Playwright Code**
```java
// Click search icon
page.locator(".search-trigger").click();

// Enter search term
page.locator("input[type='search']").fill("Rolex");

// Submit search
page.locator("input[type='search']").press("Enter");
```

---

## 📦 Product Card Selectors

### **Product Card Container**
```css
.product-item
.product-type-simple
.ast-col-xs-4
[type-product]
```

### **Product Link**
```css
a[href*='/product/']
.woocommerce-loop-product__link
```

### **Product Title**
```css
.woocommerce-loop-product__title
.product-title
h2.product-title
```

### **Product Price**
```css
.price
.amount
.woocommerce-Price-amount
```

### **Add to Cart Button**
```css
.add_to_cart_button
.ajax_add_to_cart
button.single_add_to_cart_button
```

### **Sale Badge**
```css
.onsale
.sale-badge
.badge
```

---

## 📄 Product Page Selectors

### **Product Title**
```css
h1.product_title
.product-title
[data-product-title]
```

### **Product Price**
```css
.price
.amount
.woocommerce-Price-amount
```

### **Add to Cart (Product Page)**
```css
button.single_add_to_cart_button
.single_add_to_cart_button
button:has-text('Add to cart')
```

### **Quantity Selector**
```css
input[name='quantity']
.quantity input.qty
.qty
```

### **Variation Selectors (Size/Color)**
```css
select[name*='attribute']
.variations_form
.variable-items-wrapper
```

### **Out of Stock**
```css
.out-of-stock
.stock.out-of-stock
```

---

## 🛍️ Cart Page Selectors

### **Cart Items**
```css
.cart_item
.woocommerce-cart-form__cart-item
```

### **Product Name in Cart**
```css
.product-name a
.cart-item-title
```

### **Quantity Input**
```css
input[name*='quantity']
.qty
.quantity-input
```

### **Remove Button**
```css
.remove
.product-remove a
```

### **Cart Totals**
```css
.cart-subtotal
.order-subtotal
.cart-total
.order-total
```

### **Update Cart Button**
```css
button[name='update_cart']
.update-cart-button
```

### **Checkout Button**
```css
button.checkout-button
.checkout-btn
button:has-text('Proceed to Checkout')
```

---

## 💳 Checkout Page Selectors

### **Billing Form**
```css
#billing_form
.woocommerce-billing-fields
.checkout_billing
```

### **Name Fields**
```css
#billing_first_name
#billing_last_name
input[name='billing_first_name']
```

### **Email Field**
```css
#billing_email
input[name='billing_email']
```

### **Phone Field**
```css
#billing_phone
input[name='billing_phone']
```

### **Payment Methods**
```css
/* COD */
input[value='cod']
#wrapper_payment_method_payment_method_cod

/* Prepaid */
input[value='razorpay']
input[value='paypal']
```

### **Place Order Button**
```css
#place_order
button:has-text('Place Order')
.woocommerce-checkout-payment__button
```

---

## 🔐 Login Page Selectors

### **Login Form**
```css
.woocommerce-form-login
.login-form
u-column1.col-1
```

### **Username/Email Field**
```css
input[name='username']
#username
.username
```

### **Password Field**
```css
input[name='password']
#password
.password
```

### **Login Button**
```css
button[name='login']
.woocommerce-form-login__submit
```

### **Remember Me Checkbox**
```css
input[name='rememberme']
#rememberme
```

---

## 🏷️ Brand & Category Selectors

### **Brand Links**
```css
a[href*='brand']
.brand-link
```

### **Category Links**
```css
a[href*='product-category']
.category-link
```

### **Men's Watches**
```css
a[href*='mens-watches']
.mens-watches-link
```

### **Women's Watches**
```css
a[href*='womens-watches']
.womens-watches-link
```

### **ETA Watches**
```css
a[href*='eta-watches']
.eta-watches-link
```

---

## 🎯 Best Practices for WatchStudio

1. **Use CSS Selectors** - More reliable and faster than XPath
2. **Wait for AJAX** - WooCommerce uses AJAX for cart operations
3. **Handle Multiple Matches** - Use `.first()` or more specific selectors
4. **Check for Elementor** - Many elements use Elementor classes
5. **WooCommerce Classes** - Leverage standard WC class names
6. **Avoid Fixed Waits** - Use `waitForLoadState()` instead
7. **Handle Variations** - Products may have size/material options

---

## 🧪 Testing Selectors

### **Quick Test in Browser Console**
```javascript
// Test CSS selector
document.querySelector("a.elementor-icon[href*='my-account']")

// Test XPath
document.evaluate("//a[contains(@href, 'my-account')]", document).singleNodeValue

// Count matches
document.querySelectorAll("a[href*='my-account']").length
```

### **Playwright Code Generator**
```bash
# Use Playwright's codegen to find best selectors
npx playwright codegen https://watchstudio.in
```

---

## 📝 Common Issues & Solutions

### **Issue: Multiple elements found**
**Solution:** Use more specific selector or `.first()`
```java
// Instead of
page.locator("a[href*='my-account']").click();

// Use
page.locator("a.elementor-icon[href*='my-account']").first().click();
```

### **Issue: Element not interactable**
**Solution:** Wait for element to be ready
```java
page.waitForSelector("a.elementor-icon[href*='my-account']");
page.locator("a.elementor-icon[href*='my-account']").click();
```

### **Issue: Element inside iframe**
**Solution:** Switch to iframe first
```java
FrameLocator iframe = page.frameLocator("#my-iframe");
iframe.locator("button").click();
```

---

**Last Updated:** 2026-04-23
**Framework:** Playwright Java
**Website:** WatchStudio.in
