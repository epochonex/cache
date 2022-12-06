package com.github.houbb.cache.core.bf;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BfTest {
    Funnel<Person> personFunnel = new Funnel<Person>() {
        @Override
        public void funnel(Person person, PrimitiveSink into) {
            into.putInt(person.id)
                    .putString(person.firstName, Charsets.UTF_8)
                    .putString(person.lastName, Charsets.UTF_8)
                    .putInt(person.birthYear);
        }
    };

    @Test
    public void test() {
        BloomFilter<Person> friends = BloomFilter.create(personFunnel, 500, 0.01);
        for (Person friend : personList()) {
            friends.put(friend);
        }

        Person dude = new Person(1, "1", "1", 1);

        // much later
        if (friends.mightContain(dude)) {
            // the probability that dude reached this place if he isn't a friend is 1%
            // we might, for example, start asynchronously loading things for dude while we do a more expensive exact check
            System.out.println("也许存在哦。。。");
        }
    }

    private List<Person> personList() {
        Person person = new Person(1, "1", "1", 1);
        Person person2 = new Person(2, "1", "1", 1);
        Person person3 = new Person(3, "1", "1", 1);
        Person person4 = new Person(4, "1", "1", 1);

        return Arrays.asList(person, person2, person3, person4);
    }
}
