/*
 * Copyright (C) 2017/2019 VSCT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.tock.demo.common

import ai.tock.bot.api.client.newBot
import ai.tock.bot.api.client.newStory
import ai.tock.bot.api.client.unknownStory
import ai.tock.bot.connector.web.webButton
import ai.tock.bot.connector.web.webMessage
import ai.tock.bot.definition.Intent
import ai.tock.shared.property
import net.aksingh.owmjapis.core.OWM
import net.aksingh.owmjapis.model.CurrentWeather


val apiKey = property("tock_bot_api_key", "d0e3b92c-d3ee-4b48-a7de-b693d537b61a")


//the wheather library
//get your own key api
val owm = OWM("28780fa70a9393b46dbea27bd1624f3d")


val bot = newBot(
        apiKey,
        newStory("greetings") {
            end("Hello $message")
        },
        newStory("wheather") {
            try {
                val cityEntity = this.entities.firstOrNull { e -> "city" == e.role }?.let {
                    // getting current weather data for the city
                    val cwd: CurrentWeather? = it.content?.let { content ->
                        owm.currentWeatherByCityName(content)
                    }

                    if (cwd != null) {
                        val tempMax: Int = cwd.mainData?.temp?.toInt() ?: 0
                        // to F : (( kelvinValue - 273.15) * 9/5) + 32
                        val celsius  = tempMax - 273.15;
                        end("La météo pour la ville de ${it.content} est de ${celsius.toInt()} degrés celsius")
                    } else {
                        end("Je n'ai pas pu trouver les informations météo essayez sur google ^^")
                    }
                } ?: run {
                    end("Je penses que vous devez réessayer avec le nom de la ville")
                }
                this.entities.clear()
            } catch (e: Exception) {
                end("Une erreur est survenue veillez réessayez plus tard ou consulter sur google")
            }

        },
        newStory("card") {
            //cleanup entities
            val test = entityText("location")
            entities.clear()
            end(
                    newCard(
                            test ?: "Hey",
                            "Where are you going?",
                            newAttachment("https://upload.wikimedia.org/wikipedia/commons/2/22/Heckert_GNU_white.svg"),
                            newAction("Action1"),
                            newAction("Tock", "https://doc.tock.ai")
                    )
            )
        },
        newStory("carousel") {
            end(
                    newCarousel(
                            listOf(
                                    newCard(
                                            "Card 1",
                                            null,
                                            newAttachment("https://upload.wikimedia.org/wikipedia/commons/2/22/Heckert_GNU_white.svg"),
                                            newAction("Action1"),
                                            newAction("Tock", "https://doc.tock.ai")
                                    ),
                                    newCard(
                                            "Card 2",
                                            null,
                                            newAttachment("https://doc.tock.ai/fr/images/header.jpg"),
                                            newAction("Action1"),
                                            newAction("Tock", "https://doc.tock.ai")
                                    )
                            )
                    )
            )
        },
        unknownStory {
            end {
                //custom model sample
                webMessage(
                        "Sorry - not understood",
                        webButton("Card", Intent("card")),
                        webButton("Carousel", Intent("carousel"))
                )
            }
        }
)
