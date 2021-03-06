package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        FieldError priceError = errors.getFieldError("price");
        log.info("price={}", errors.getFieldError("price"));
        FieldError quantityError = errors.getFieldError("quantity");

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            errors.rejectValue("itemName",null,null,"필수 값");
        }

        if(priceError==null && (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)){
            errors.rejectValue("price","range", new Object[]{1000, 1000000}, null);
        }
        if(quantityError==null && (item.getQuantity() == null || item.getQuantity() >= 9999)){
            errors.rejectValue("quantity","max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null)
        {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
            }
        }
    }



}
