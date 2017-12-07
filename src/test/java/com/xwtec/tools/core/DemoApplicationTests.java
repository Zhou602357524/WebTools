package com.xwtec.tools.core;

import com.xwtec.tools.core.entity.PushEntity;
import com.xwtec.tools.core.repository.PushRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	private PushRepository pushRepository;
	@Test
	public void contextLoads() {

		ArrayList<PushEntity> arr = new ArrayList<>();
		for (int i = 0; i < 500; i++) {
			PushEntity pushEntity = new PushEntity();
			pushEntity.setPhone(String.valueOf(i));
			arr.add(pushEntity);
		}


		pushRepository.save(arr);

	}

}
