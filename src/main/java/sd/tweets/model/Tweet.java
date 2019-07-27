package sd.tweets.model;

import lombok.Data;
import sd.config.users.model.User;

@Data
public class Tweet {

    private String message;
    private User user;
    private String image;
    private String date;

}
