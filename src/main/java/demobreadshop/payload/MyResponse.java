package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyResponse {
    public static final MyResponse USER_NOT_FOUND = new MyResponse("User not found", false);
    public static final MyResponse WRONG_PASSWORD = new MyResponse("Password doesnt match", false);
    public static final MyResponse PHONE_NUMBER_EXISTS = new MyResponse("The phone number already taken", false);
    public static final MyResponse FULL_NAME_EXISTS = new MyResponse("The fullName already taken", false);
    public static final MyResponse ROLE_NOT_FOUND = new MyResponse("The role not found", false);
    public static final MyResponse SUCCESSFULLY_CREATED = new MyResponse("Successfully created", true);
    public static final MyResponse MATERIAL_NAME_EXISTS = new MyResponse("The product has already created", false);
    public static final MyResponse PRODUCT_NOT_FOUND = new MyResponse("The product not found", false);
    public static final MyResponse SUCCESSFULLY_UPDATED = new MyResponse("Successfully updated", true);
    public static final MyResponse CANT_DELETE = new MyResponse("You can't delete this item", false);
    public static final MyResponse SUCCESSFULLY_DELETED = new MyResponse("Deleted successfully", true);
    public static final MyResponse INPUT_NOT_FOUND = new MyResponse("The input not found", false);
    public static final MyResponse CLIENT_NOT_FOUND = new MyResponse("The client not found", false);
    public static final MyResponse SALE_NOT_FOUND =  new MyResponse("The sale not found", false);
    public static final MyResponse INPUT_TYPE_ERROR = new MyResponse("The input type error", false);
    public static final MyResponse CAN_DELETE_OWN = new MyResponse("Only you can delete your own item !", false);
    public static final MyResponse SUCCESSFULLY_PAYED = new MyResponse("Successfully paid", true);
    public static final MyResponse DELIVERY_NOT_FOUND = new MyResponse("The deliverer not found", false);
    public static final MyResponse SUCCESSFULLY_DELIVERED = new MyResponse("The delivery successfully delivered", true);
    public static final MyResponse YOU_DONT_HAVE_THIS_PRODUCT = new MyResponse("Currently you don't have this product, please check your balance", false);
    public static final MyResponse OUTCOME_NOT_FOUND = new MyResponse("The outcome not found", false);
    public static final MyResponse YOU_CANT_CREATE = new MyResponse("You can't create this item", false);
    public static final MyResponse COMPLAINT_NOT_FOUND = new MyResponse("Complaint not found", false);
    public static final MyResponse FILE_NOT_FOUND = new MyResponse("Photo hasn't uploaded", false);
    public static final MyResponse ARENT_WORKER = new MyResponse("Only workers include", false);
    public static final MyResponse WORKER_NOT_FOUND = new MyResponse("Worker not found", false);
    public static final MyResponse YOU_HAVEN_T_ACCESS = new MyResponse("You don't have access", false);
    public static final MyResponse PAYMENT_NOT_FOUND = new MyResponse("Payment Not Found", false);
    public static final MyResponse SUCCESSFULLY_TRANSFERRED = new MyResponse("Product successfully transferred, but waited", true);
    public static final MyResponse SUCCESSFULLY_MOVED = new MyResponse("Product successfully moved ", true);

    private String message;
    private boolean active;
}
