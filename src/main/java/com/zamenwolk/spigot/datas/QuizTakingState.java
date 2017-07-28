/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.zamenwolk.spigot.datas;

import java.io.Serializable;

/**
 * Author: Martin
 * created on 28/07/2017.
 */
public enum QuizTakingState implements Serializable
{
    NOT_TAKING_QUIZ,
    PRELIMINARY_QUESTIONS,
    TAKING_QUIZ,
    SCHOOL_PICKING,
    QUIZ_TAKEN
}
