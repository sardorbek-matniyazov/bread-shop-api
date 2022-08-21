package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyResponse {
    public static final MyResponse USER_NOT_FOUND = new MyResponse("User not found", false, null);
    public static final MyResponse WRONG_PASSWORD = new MyResponse("The Password doesn't match", false, null);
    public static final MyResponse PHONE_NUMBER_EXISTS = new MyResponse("The phone number already taken", false, null);
    public static final MyResponse FULL_NAME_EXISTS = new MyResponse("The full name already taken", false, null);
    public static final MyResponse ROLE_NOT_FOUND = new MyResponse("The Role not found", false, null);
    public static final MyResponse SUCCESSFULLY_CREATED = new MyResponse("Created successfully", true, null);
    public static final MyResponse MATERIAL_NAME_EXISTS = new MyResponse("The Material already created", false, null);
    public static final MyResponse PRODUCT_NOT_FOUND = new MyResponse("The product not found", false, null);
    public static final MyResponse SUCCESSFULLY_UPDATED = new MyResponse("Updated successfully", true, null);
    public static final MyResponse CANT_DELETE = new MyResponse("You cant delete this item", false, null);
    public static final MyResponse SUCCESSFULLY_DELETED = new MyResponse("Deleted Successfully", true, null);
    public static final MyResponse INPUT_NOT_FOUND = new MyResponse("The Input not found", false, null);
    public static final MyResponse CLIENT_NOT_FOUND = new MyResponse("The Client not found", false, null);;
    public static final MyResponse SALE_NOT_FOUND =  new MyResponse("The Sale not found", false, null);

    private String message;
    private boolean active;
    private Object any;
}
