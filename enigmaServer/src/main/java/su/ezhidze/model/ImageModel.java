package su.ezhidze.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageModel {

    private String image;

    public ImageModel(String image) {
        this.image = image;
    }
}
