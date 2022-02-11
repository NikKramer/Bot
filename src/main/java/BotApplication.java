import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import lombok.SneakyThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


//в хэндлере обработать /start для приветствия

public class BotApplication extends TelegramLongPollingBot {
    public String getBotUsername() {
        return "@Allcco_bot";
    }

    public String getBotToken() {
        return "5019912278:AAECmYizBvhKII74RKiGgHraIK5CAC7Jyuw";
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            if(message.hasText()) {
                try {
                    MessageEntity commandEntity =
                            message.getEntities().stream()
                                    .filter(e -> "bot_command".equals(e.getType()))
                                    .findFirst()
                                    .orElseThrow(
                                            () -> new RuntimeException("Error during extracting Command Entity")
                                    );
                    List<List<InlineKeyboardButton>> keyboard = HandleMessage(update.getMessage());
                } catch (RuntimeException emptyException) {
                    try {
                        execute(SendMessage.builder().text("Ничего не понял, алкаш сраный. Введите команду /set_weight 'вес'").chatId(message.getChatId().toString()).build());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                }
            }
        }


    @SneakyThrows
    public List<List<InlineKeyboardButton>> HandleMessage(Message message){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        if(message.hasText() && message.hasEntities()){
            Optional<MessageEntity> CommandEntity = message.getEntities().stream().filter(e->"bot_command".equals(e.getType())).findFirst();
            if (CommandEntity.isPresent()){
                String command = message.getText().substring(CommandEntity.get().getOffset(),CommandEntity.get().getLength());
                            switch (command){
                case "/set_weight":
                    buttons.add(Arrays.asList(
                            InlineKeyboardButton.builder().text("М").callbackData("man").build(),
                            InlineKeyboardButton.builder().text("Ж").callbackData("woman").build()));
                            execute(SendMessage.builder().text("Укажите свой пол").chatId(message.getChatId().toString()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).build());
                            List<List<InlineKeyboardButton>> buttonsDrinks = new ArrayList<>();
                            buttonsDrinks.add(Arrays.asList(InlineKeyboardButton.builder().text(Alcohol.Водка.name()).callbackData("Vodka").build(),
                                    InlineKeyboardButton.builder().text(Alcohol.Пиво.name()).callbackData("Beer").build(),
                                    InlineKeyboardButton.builder().text(Alcohol.Виски.name()).callbackData("Whiskey").build(),
                                    InlineKeyboardButton.builder().text(Alcohol.Коньяк.name()).callbackData("Brandy").build()));
                                    execute(SendMessage.builder().text("Выберете, что вы пили").chatId(message.getChatId().toString()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttonsDrinks).build()).build());
                            return buttonsDrinks;
                    case "/start":
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(
                                "Приветствую, выпивоха! Меня зовут АлкоБот, я помогу тебе определить, когда ты вновь будешь трезвым, например, чтобы сесть за руль.\n" +
                                        "Чтобы начать, введите команду '/set_weight вес'").build());
                            }
            }
        }
        return buttons;
    }

    @SneakyThrows
    public void HandlerCallback(CallbackQuery callbackQuery){
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();
    }

    public float Converter(Alcohol alcohol, int mess){
    return mess;
    }

    public static void main(String[] args) throws TelegramApiException {
        BotApplication Bot = new BotApplication();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(Bot);

    }
}