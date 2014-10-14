/*
 * Copyright  2013 Egor Smirnov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.game.frankenstain.test;

import org.junit.Assert;
import org.junit.Test;
import ru.game.frankenstein.*;
import ru.game.frankenstein.impl.MonsterGenerationContext;
import ru.game.frankenstein.awt.imageio.BufferedImageFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestContext
{
    @Test
    public void testFilter() throws FrankensteinException {

        Map<MonsterPartType, Collection<MonsterPart>> parts = new HashMap<MonsterPartType, Collection<MonsterPart>>();
        Collection<MonsterPart> coll = new ArrayList<MonsterPart>();
        coll.add(new MonsterPart(MonsterPartType.MONSTER_BODY, null, "", new String[] {"herbivore"}, null));
        coll.add(new MonsterPart(MonsterPartType.MONSTER_BODY, null, "", new String[]{"predator"}, null));
        parts.put(MonsterPartType.MONSTER_BODY, coll);
        MonsterPartsSet set = new MonsterPartsSet(null, parts, null, null);

        MonsterGenerationContext context = new MonsterGenerationContext(set, new BufferedImageFactory(), new MonsterGenerationParams(false, false, "predator"));

        Assert.assertEquals(1, context.getSuitableParts().get(MonsterPartType.MONSTER_BODY).size());
    }
}
