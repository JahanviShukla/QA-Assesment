# Test Scenarios Document - SuperKicks.in E-commerce

**Application Under Test:** https://www.superkicks.in/
**Test Framework:** Playwright with Java
**Document Version:** 1.0
**Date:** 2025-04-23

---

## 1. Core Flow Test Scenarios

### 1.1 Login Flow
**Scenario ID:** TC-001
**Description:** Verify user can successfully login with valid credentials

**Pre-conditions:**
- User has a registered account
- Application is accessible

**Test Steps:**
1. Navigate to https://www.superkicks.in/
2. Click on Account/Login icon
3. Enter valid email address
4. Enter valid password
5. Click on Login button
6. Verify user is logged in successfully

**Expected Results:**
- Login is successful
- User name/account is displayed in header
- User can access account features

**State Transition:** Guest → Authenticated User

---

### 1.2 Browse and Select Product
**Scenario ID:** TC-002
**Description:** Verify user can browse products and select items

**Pre-conditions:**
- User is on homepage
- Products are available

**Test Steps:**
1. Navigate to homepage
2. Browse through categories or use search
3. Select a product category
4. Click on a specific product
5. Verify product details page loads

**Expected Results:**
- Product listing displays correctly
- Product details page shows:
  - Product images
  - Product name
  - Price
  - Size options (if applicable)
  - Add to Cart button

**State Transition:** Homepage → Product Listing → Product Details

---

### 1.3 Add to Cart - Single Item
**Scenario ID:** TC-003
**Description:** Verify user can add a single item to cart

**Pre-conditions:**
- User is on a product details page
- Item is in stock

**Test Steps:**
1. Select size (if required)
2. Click "Add to Cart" button
3. Wait for cart confirmation
4. Verify cart icon shows updated count

**Expected Results:**
- Item is added to cart
- Cart counter increments by 1
- Success message or visual confirmation appears
- Cart total reflects item price

**State Transition:** Cart Empty → Cart Has Items

---

### 1.4 Modify Cart - Increase Quantity
**Scenario ID:** TC-004
**Description:** Verify user can increase item quantity in cart

**Pre-conditions:**
- User has items in cart
- User is on cart page

**Test Steps:**
1. Navigate to cart
2. Identify item with quantity 1
3. Increase quantity to 2
4. Verify cart updates

**Expected Results:**
- Quantity updates to 2
- Cart total doubles for that item
- Subtotal reflects correct amount
- No duplicate items created

**State Transition:** Cart Quantity Modified

---

### 1.5 Modify Cart - Decrease Quantity
**Scenario ID:** TC-005
**Description:** Verify user can decrease item quantity in cart

**Pre-conditions:**
- User has item with quantity > 1 in cart
- User is on cart page

**Test Steps:**
1. Navigate to cart
2. Identify item with quantity > 1
3. Decrease quantity by 1
4. Verify cart updates

**Expected Results:**
- Quantity decreases correctly
- Cart total decreases proportionally
- Item remains in cart if quantity > 0

**State Transition:** Cart Quantity Modified

---

### 1.6 Remove Item from Cart
**Scenario ID:** TC-006
**Description:** Verify user can remove items from cart

**Pre-conditions:**
- User has items in cart
- User is on cart page

**Test Steps:**
1. Navigate to cart
2. Click remove button for an item
3. Confirm removal if prompted
4. Verify cart updates

**Expected Results:**
- Item is removed from cart
- Cart counter decreases
- Cart total updates
- Other items remain unaffected

**State Transition:** Cart Has Items → Cart Empty/Fewer Items

---

### 1.7 Logout Flow
**Scenario ID:** TC-007
**Description:** Verify user can logout successfully

**Pre-conditions:**
- User is logged in
- User has items in cart (optional)

**Test Steps:**
1. Click on account menu
2. Select Logout option
3. Verify logout is complete

**Expected Results:**
- User is logged out
- Session is terminated
- Redirect to homepage or login page
- Cart state handling depends on implementation

**State Transition:** Authenticated User → Guest

---

### 1.8 State Persistence - Cart After Logout/Login
**Scenario ID:** TC-008
**Description:** Verify cart state persists across sessions

**Pre-conditions:**
- User is logged in
- User has items in cart

**Test Steps:**
1. Add items to cart (while logged in)
2. Note cart contents and total
3. Logout
4. Login again with same credentials
5. Navigate to cart
6. Verify cart contents

**Expected Results:**
- Cart contents are preserved
- Quantities remain same
- Prices remain same
- Total remains same

**State Transition:** Session Termination → New Session → State Restoration

---

## 2. Edge Case Test Scenarios

### 2.1 Duplicate Action - Double Click Add to Cart
**Scenario ID:** TC-EDGE-001
**Description:** Verify system handles rapid double-click on Add to Cart

**Pre-conditions:**
- User is on product page
- Item is in stock

**Test Steps:**
1. Select size (if required)
2. Double-click "Add to Cart" button rapidly
3. Wait for all responses to complete
4. Check cart contents

**Expected Results:**
- Only 1 item is added (idempotent behavior)
- OR 2 items are added (acceptable if intentional)
- System doesn't crash or show errors
- Cart state is consistent

**State Validation:** System handles concurrent requests gracefully

---

### 2.2 Page Refresh During Add to Cart
**Scenario ID:** TC-EDGE-002
**Description:** Verify system handles page refresh during cart operations

**Pre-conditions:**
- User is on product page

**Test Steps:**
1. Click "Add to Cart"
2. Immediately refresh page (F5 or browser refresh)
3. Navigate to cart
4. Verify cart state

**Expected Results:**
- Either: Item is added successfully
- Or: Add operation is cancelled cleanly
- No partial states or corruption
- Cart shows consistent state

**State Validation:** System handles interruption gracefully

---

### 2.3 Delayed UI Updates
**Scenario ID:** TC-EDGE-003
**Description:** Verify correct behavior when UI updates are delayed

**Pre-conditions:**
- User is on product page
- Network is slow (simulated)

**Test Steps:**
1. Click "Add to Cart"
2. Observe UI states during API call
3. Verify loading indicators (if any)
4. Wait for completion
5. Verify final cart state

**Expected Results:**
- Loading state is shown (if implemented)
- Button is disabled during processing (ideal)
- Final state matches actual backend state
- No misleading intermediate states

**State Validation:** UI state consistency with backend

---

### 2.4 Session Expiry During Cart Operations
**Scenario ID:** TC-EDGE-004
**Description:** Verify behavior when session expires during cart operations

**Pre-conditions:**
- User is logged in
- User has items in cart
- Session timeout is configured

**Test Steps:**
1. Add items to cart
2. Wait for session to expire (or simulate)
3. Attempt to modify cart
4. Observe system behavior

**Expected Results:**
- User is redirected to login
- Clear error message about session expiry
- Cart state is preserved (for logged-in users)
- OR cart is cleared (for guest users - acceptable)

**State Validation:** Session timeout handling

---

### 2.5 Add Same Item Multiple Times
**Scenario ID:** TC-EDGE-005
**Description:** Verify behavior when adding same item multiple times

**Pre-conditions:**
- User is on product page
- Item may already be in cart

**Test Steps:**
1. Add item to cart
2. Navigate to same product again
3. Add same item (with same size/variant) again
4. Check cart

**Expected Results:**
- Quantity increases (preferred)
- OR separate line item created (acceptable)
- No duplicates created unintentionally
- Total price is correct

**State Validation:** Idempotent add operations

---

### 2.6 Cart Operations with Multiple Browser Sessions
**Scenario ID:** TC-EDGE-006
**Description:** Verify cart consistency across multiple sessions for same user

**Pre-conditions:**
- User has valid credentials
- Two browser contexts available

**Test Steps:**
1. Open Session A and login
2. Add item to cart in Session A
3. Open Session B and login with same credentials
4. Check cart in Session B
5. Modify cart in Session B
6. Refresh Session A and check cart

**Expected Results:**
- Cart is synchronized across sessions
- Changes in one session reflect in other
- Last write wins (or optimistic locking)
- No data corruption
- Real-time updates (if implemented) or refresh updates

**State Validation:** Concurrent session handling

---

## 3. State Transition Matrix

| From State | To State | Trigger | Validation Points |
|------------|----------|---------|-------------------|
| Guest | Authenticated | Successful login | Session token, user info display, cart merge (if applicable) |
| Authenticated | Guest | Logout | Session cleared, redirect, cart handling |
| Cart Empty | Cart Has Items | Add to cart | Cart count, total, item details |
| Cart Has Items | Cart Empty | Remove all items | Cart count zero, empty state message |
| Single Item | Multiple Items | Add more items | Quantity updates, totals correct |
| Multiple Items | Single Item | Remove items | Remaining items correct, totals updated |
| Active Session | Expired Session | Timeout/inactivity | Redirect, error message, state preservation |

---

## 4. Test Data Requirements

### Valid User Credentials
- Email: [To be configured]
- Password: [To be configured]

### Test Product Selection
- Category: Footwear (Sneakers)
- Product: Any in-stock item
- Size: Standard size (e.g., UK 7, US 8)

### Test Quantities
- Initial: 1
- Modified: 2, 3, 5
- Edge: 0 (remove), 10+ (bulk)

---

## 5. API Endpoints to Monitor

1. **Authentication:**
   - POST /login or /account/login
   - POST /logout

2. **Cart Operations:**
   - GET /cart or /api/cart
   - POST /cart/add or /api/cart/items
   - PUT /cart/update or /api/cart/items/{id}
   - DELETE /cart/remove or /api/cart/items/{id}

3. **Product Catalog:**
   - GET /products or /api/products
   - GET /products/{id} or /api/products/{id}

---

## 6. Success Criteria

- All core flow scenarios pass
- Edge cases are handled gracefully
- No fixed waits in automation
- Proper Page Object Model implementation
- Comprehensive logging and reporting
- API validation complete
- Multi-session testing completed
- State persistence validated

---

## 7. Known Limitations & Assumptions

1. **Authentication:** Assuming standard email/password login is available
2. **Cart Persistence:** Assuming cart is saved for logged-in users
3. **Session Sync:** Real-time sync may not be implemented; manual refresh may be needed
4. **Network Conditions:** Tests will simulate slow networks for delayed UI testing
5. **Test Data:** Using configurable test data; actual credentials to be provided separately

---

## 8. Sign-off

**Test Scenarios Designed By:** QA Engineer
**Date:** 2025-04-23
**Status:** Ready for Automation Implementation
