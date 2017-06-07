package com.thoughtworks.petstore;

import com.thoughtworks.petstore.core.pet.Pet;
import com.thoughtworks.petstore.core.pet.PetRepository;
import com.thoughtworks.petstore.core.user.Email;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserRepository;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootPetStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPetStoreApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(PetRepository petRepository, UserRepository userRepository) {
		return args -> {
			Pet americanShorthair = new Pet(
				"美国短毛猫",
				"原产美国的一种猫，其祖先为欧洲早期移民带到北美的猫种，与英国短毛猫和欧洲短毛猫同类。据记载，五月花号上携带了数只猫以帮助除鼠。该品种的猫是在街头巷尾收集来的猫当中选种、并和进口品种如英国短毛猫、缅甸猫和波斯猫杂交培育而成。美国短毛猫在欧洲很罕见，但在日本颇受好评，在美国国内也较受欢迎。",
				Money.of(CurrencyUnit.of("CNY"), 4000), 10);
			Pet husky = new Pet(
				"哈士奇",
				"哈士奇（英语：Husky）是对在北方地区用来拉雪橇的一大类狗的通称，它们因为能够快速拉雪橇而与其它种类的雪橇犬分开来。“它们种类繁多，基因上多为杂交，速度很快”。相比之下，阿拉斯加雪橇犬是“最大和最强壮的”雪橇犬，通常用于沉重的任务。哈士奇也用于雪橇犬比赛。最近几年也被有的公司用来在有雪的区域为旅游的人们拉雪橇观光。 哈士奇也可作为宠物。",
				Money.of(CurrencyUnit.of("CNY"), 4000), 20);
			Pet toyPoodl = new Pet(
				"泰迪",
				"对于标准型、迷你型、玩具型的贵宾犬来说，各项指标的标准都是一样的，除了高度。 外形、姿态和状态 贵宾犬是很活跃、机警而且行动优雅的犬种，拥有很好的身体比例和矫健的动作，显示出一种自信的姿态。经过传统方式修剪和仔细的梳理后，贵宾犬会显示出与生俱来的独特而又高贵的气质。 整体外型：一种中等比例，身体协调的狗，具有卷曲或扎捆特有的特有卷毛。外表聪明，警惕，活泼，比例匀称，给人以优雅高贵的印象。",
				Money.of(CurrencyUnit.of("CNY"), 4000), 10
			);
			Pet chihuahua = new Pet(
				"吉娃娃",
				"吉娃娃属小型犬种里最小的 ,优雅，警惕，动作迅速，以匀称的体格和娇小的体型广受人们的喜爱。吉娃娃犬不仅是可爱的小型玩具犬，同时也具备大型犬的狩猎与防范本能，具有类似梗类犬的气质。此犬分为长毛种和短毛种。这种狗体型娇小,对其它狗不胆怯,对主人极有独占心。短毛种和长毛种不同之处在于富有光泽,贴身,柔顺的短被毛。长毛种的吉娃娃除了背毛丰厚外,像短毛种一样具有发抖的倾向,不要认为是感冒。 整体外观：这种犬身体紧凑。很重要的一点是其头骨为苹果形，尾巴长度适当且高高举起，不卷曲也不成半圆形，尾尖指向腰部。 重要比例：体长各大于从地面到马肩隆的高度。体形最好几近方形，雄性更是如此。雌性由于其生育特点，其体长可以更长一些。",
				Money.of(CurrencyUnit.of("CNY"), 4000), 30
			);

			petRepository.save(americanShorthair);
			petRepository.save(husky);
			petRepository.save(toyPoodl);
			petRepository.save(chihuahua);

			User admin = User.admin("aisensiy", new Email("aisensiy@163.com"), "demo");
			userRepository.save(admin);
		};
	}
}
