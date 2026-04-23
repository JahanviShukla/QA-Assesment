# WatchStudio.in - Test Case Document

## Feature: End-to-End E-commerce Workflow with State Persistence

**Application:** WatchStudio.in (WordPress/WooCommerce)
**Focus:** State transitions, guest checkout, cart persistence, brand navigation

---

## 1. GUEST USER FLOW (WatchStudio Primary Flow)

### Scenario: Browse watches as guest user
**Given** user is on the WatchStudio homepage
**When** user navigates to Men's Watches category
**Then** products should be displayed
**And** user should not be required to login

### Scenario: Search for specific watch brand
**Given** user is on the homepage
**When** user searches for "Rolex"
**Then** relevant Rolex watches should be displayed
**And** results should contain brand name

### Scenario: Navigate by brands
**Given** user is on the homepage
**When** user clicks on "Brands" section
**And** selects "ROLEX" brand
**Then** only Rolex watches should be displayed
**And** product count should be visible

### Scenario: View sale products
**Given** user is on the homepage
**When** user looks for products with sale badges
**Then** discounted prices should be visible
**And** original prices should be struck through

---

## 2. PRODUCT SELECTION & DETAILS

### Scenario: Open product from listing
**Given** user is on product listing page
**When** user clicks on a product
**Then** product details page should open
**And** product name, price, and images should be visible

### Scenario: Check product availability
**Given** user is on product details page
**When** product is in stock
**Then** "Add to Cart" button should be enabled
**And** quantity selector should be visible

### Scenario: Out-of-stock product handling
**Given** user is on out-of-stock product page
**When** user tries to add to cart
**Then** system should show "Out of Stock" message
**And** add to cart should be disabled

### Scenario: Product with variations (if applicable)
**Given** product has size/material variations
**When** user selects a variation
**Then** price should update if different
**And** add to cart should become enabled

### Scenario: Verify product information
**Given** user is on product details page
**Then** following information should be visible:
  - Product name
  - Price (original and sale price if applicable)
  - Product description
  - Product images
  - SKU code (if available)
  - Categories/Brands

---

## 3. ADD TO CART FLOW

### Scenario: Add single product to cart
**Given** user is on product details page
**When** user clicks "Add to Cart"
**Then** product should be added to cart
**And** cart count should increase to 1
**And** success message should be displayed

### Scenario: Add multiple quantities
**Given** user is on product details page
**When** user sets quantity to 3
**And** clicks "Add to Cart"
**Then** 3 items should be added to cart
**And** total price should reflect quantity × unit price

### Scenario: Add to cart from product listing
**Given** user is on product listing page
**When** user clicks "Add to Cart" on a product
**Then** product should be added to cart
**And** cart should update via AJAX

### Scenario: Rapid add to cart clicks
**Given** user is on product details page
**When** user clicks "Add to Cart" multiple times rapidly
**Then** system should prevent duplicate additions
**And** cart count should be accurate
**And** no inconsistent state should occur

### Scenario: Add to cart state transition
**Given** cart is empty
**When** user adds first product
**Then** cart state should transition from empty → non-empty
**And** cart icon should show count badge
**And** mini-cart should appear (if configured)

---

## 4. CART MANAGEMENT

### Scenario: View cart contents
**Given** user has products in cart
**When** user clicks on cart icon
**Then** cart page should display all items
**And** following should be visible for each item:
  - Product name
  - Price
  - Quantity
  - Subtotal
  - Remove button

### Scenario: Increase quantity in cart
**Given** user has 1 item in cart with quantity 1
**When** user increases quantity to 2
**Then** quantity should update to 2
**And** item subtotal should double
**And** cart total should update correctly

### Scenario: Decrease quantity in cart
**Given** user has 1 item in cart with quantity 3
**When** user decreases quantity to 2
**Then** quantity should update to 2
**And** item subtotal should reduce
**And** cart total should update correctly

### Scenario: Set quantity to zero removes item
**Given** user has 1 item in cart
**When** user sets quantity to 0
**Then** item should be removed from cart
**And** cart should become empty
**And** empty cart message should display

### Scenario: Remove item from cart
**Given** user has products in cart
**When** user clicks remove button on an item
**Then** item should be removed immediately
**And** cart should update total
**And** cart should reflect new state

### Scenario: Cart price validation
**Given** user has multiple items in cart
**When** cart displays totals
**Then** subtotal should equal sum of all item subtotals
**And** calculations should be accurate

### Scenario: Update cart button (if applicable)
**Given** user changes quantities
**When** user clicks "Update Cart" button
**Then** cart should refresh with new quantities
**And** totals should recalculate

---

## 5. CHECKOUT INITIATION

### Scenario: Proceed to checkout from cart
**Given** user has items in cart
**When** user clicks "Proceed to Checkout"
**Then** checkout page should load
**And** billing form should be visible
**And** order summary should be visible

### Scenario: Guest checkout availability
**Given** user is on checkout page
**When** user is not logged in
**Then** guest checkout option should be available
**And** user should be able to fill details without login

### Scenario: Payment method selection
**Given** user is on checkout page
**Then** following payment options should be visible:
  - Cash on Delivery (COD) with 10% advance note
  - Prepaid payment options (Paytm, GPay, PhonePe)
  - Card payment option

### Scenario: COD terms visibility
**Given** user selects COD payment
**Then** system should display:
  - 10% advance payment requirement
  - Non-refundable advance note
  - Remaining amount on delivery

---

## 6. SESSION & PERSISTENCE (With Login)

### Scenario: Cart persistence across sessions (logged in user)
**Given** user is logged in
**And** user has items in cart
**When** user closes browser and opens again
**And** navigates to cart
**Then** cart should retain all items
**And** quantities should be preserved

### Scenario: Cart persistence after logout
**Given** user is logged in with items in cart
**When** user logs out
**Then** cart items should be saved
**When** user logs in again
**Then** cart should be restored
**And** all items should be visible

### Scenario: Guest cart vs logged-in cart merge
**Given** guest user has items in cart
**When** user logs in
**Then** guest cart should merge with logged-in cart
**Or** system should prompt to keep either cart

---

## 7. EDGE CASES & STATE TRANSITIONS

### Scenario: Duplicate add to cart action
**Given** user is on product details page
**When** user double-clicks "Add to Cart" rapidly
**Then** only one request should process
**And** cart should show correct quantity
**And** no duplicate entries should occur

### Scenario: Page refresh during add to cart
**Given** user clicks "Add to Cart"
**When** user refreshes page immediately
**Then** cart should reflect correct final state
**And** no duplicate items should exist
**And** AJAX request should complete or cancel gracefully

### Scenario: Delayed network response handling
**Given** user clicks "Add to Cart"
**And** network response is delayed
**Then** UI should show loading indicator
**And** cart should not update until success
**And** user should not be able to click again during wait

### Scenario: Session expiry during cart operation
**Given** user is logged in
**And** user's session expires
**When** user tries to modify cart
**Then** system should detect session expiry
**And** redirect to login page
**And** cart should remain in saved state

### Scenario: Cart state consistency after navigation
**Given** user has items in cart
**When** user navigates to different pages
**And** returns to cart
**Then** cart should show same state
**And** quantities should be unchanged

### Scenario: Concurrent cart modification (multiple tabs)
**Given** user has cart open in two tabs
**When** user adds item in first tab
**And** modifies quantity in second tab
**Then** final state should be consistent
**And** last action should prevail
**And** no data corruption should occur

### Scenario: Invalid quantity input
**Given** user is on cart page
**When** user enters invalid quantity (negative, text, special chars)
**Then** input should be rejected or corrected
**And** cart should not update with invalid value

### Scenario: Maximum quantity limit
**Given** product has stock limit
**When** user tries to add more than available quantity
**Then** system should restrict quantity
**And** maximum available quantity message should display

### Scenario: Browser back button after add to cart
**Given** user adds product to cart
**When** user clicks browser back button
**Then** cart state should be preserved
**And** product should remain in cart

---

## 8. SEARCH & FILTER SCENARIOS

### Scenario: Search by product name
**Given** user is on the homepage
**When** user searches for "Daytona"
**Then** relevant products should display
**And** results should contain search term

### Scenario: Search with no results
**Given** user is on the homepage
**When** user searches for "xyznonexistent"
**Then** "No products found" message should display
**And** search suggestions should appear

### Scenario: Filter by price range
**Given** user is on product listing page
**When** user applies price filter
**Then** products within range should display
**And** filter should be active

### Scenario: Sort products by price
**Given** user is on product listing page
**When** user sorts by "Price: Low to High"
**Then** products should be ordered by price ascending
**And** sort option should be selected

---

## 9. BRAND & CATEGORY NAVIGATION

### Scenario: Navigate to Men's Watches
**Given** user is on the homepage
**When** user clicks "Men's Watches"
**Then** only men's watches should display
**And** category should be highlighted

### Scenario: Navigate to Women's Watches
**Given** user is on the homepage
**When** user clicks "Women's Watches"
**Then** only women's watches should display
**And** category should be highlighted

### Scenario: Navigate to specific brand
**Given** user is on the homepage
**When** user clicks "Brands"
**And** selects "OMEGA"
**Then** only Omega watches should display
**And** brand filter should be active

### Scenario: Navigate to ETA Watches
**Given** user is on the homepage
**When** user clicks "ETA Watches"
**Then** only Swiss ETA watches should display
**And** premium badge should be visible

---

## 10. ERROR HANDLING & VALIDATIONS

### Scenario: Invalid coupon code
**Given** user has items in cart
**When** user applies invalid coupon code
**Then** "Coupon does not exist" error should display
**And** cart total should not change

### Scenario: Expired coupon code
**Given** user has items in cart
**When** user applies expired coupon
**Then** "Coupon has expired" error should display
**And** discount should not apply

### Scenario: Valid coupon code application
**Given** user has items in cart
**When** user applies valid coupon "FLAT10"
**Then** 10% discount should apply
**And** cart total should reduce
**And** success message should display

### Scenario: Form validation on checkout
**Given** user is on checkout page
**When** user submits form with empty required fields
**Then** validation errors should display
**And** user should not proceed to payment

---

## 11. PERFORMANCE & USABILITY

### Scenario: Cart loading performance
**Given** user has items in cart
**When** user navigates to cart page
**Then** page should load within 3 seconds
**And** all items should render correctly

### Scenario: Add to cart responsiveness
**Given** user clicks "Add to Cart"
**Then** action should complete within 2 seconds
**And** loading indicator should show
**And** success message should display

### Scenario: Mobile responsive cart
**Given** user is on mobile device
**When** user views cart
**Then** cart should be mobile-optimized
**And** all actions should be accessible

---

## Test Coverage Matrix

| Feature | Scenarios | Priority | Status |
|---------|-----------|----------|--------|
| Guest Browsing | 4 | High | ✅ Covered |
| Product Selection | 6 | High | ✅ Covered |
| Add to Cart | 5 | Critical | ✅ Covered |
| Cart Management | 7 | Critical | ✅ Covered |
| Checkout Flow | 4 | High | ✅ Covered |
| Session Persistence | 3 | High | ✅ Covered |
| Edge Cases | 10 | High | ✅ Covered |
| Search & Filter | 3 | Medium | ✅ Covered |
| Brand Navigation | 4 | Medium | ✅ Covered |
| Error Handling | 4 | High | ✅ Covered |
| Performance | 3 | Medium | ✅ Covered |

**Total Scenarios:** 53
**Critical Scenarios:** 12
**High Priority:** 28
**Medium Priority:** 13

---

## State Transition Matrix

| Current State | Action | Next State | Validation |
|---------------|--------|------------|------------|
| Empty Cart | Add Item | Non-Empty Cart | Count badge visible |
| Non-Empty Cart | Remove All | Empty Cart | Empty message |
| Guest User | Login | Logged-In User | Cart merges/saves |
| Logged-In | Logout | Guest User | Cart persists |
| Product Page | Add to Cart | Cart Updated | Success message |
| Cart Page | Update Quantity | Cart Recalculated | Total updates |
| Cart Page | Apply Coupon | Discount Applied | Total reduces |
| Checkout | Submit Order | Order Placed | Confirmation page |

---

## Notes for Automation

1. **No Forced Login:** WatchStudio allows complete flow as guest
2. **COD Advance Payment:** 10% advance is non-refundable
3. **AJAX Cart:** Cart updates via AJAX, wait for network idle
4. **WooCommerce Selectors:** Use WC-specific selectors
5. **Variable Products:** Handle size/material variations
6. **Sale Badges:** Test discounted vs original prices
7. **Brand Navigation:** Major feature, test thoroughly
8. **Session Storage:** WooCommerce uses local storage for guest carts
9. **Mobile Responsive:** Test on different viewports
10. **Payment Gateways:** Multiple options, test each

---

**Document Version:** 1.0
**Last Updated:** 2026-04-23
**Status:** ✅ Ready for Automation
