/*
 * Copyright (c) 2002-2018 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler.v3_5.planner.logical.plans.rewriter

import org.neo4j.cypher.internal.compiler.v3_5.planner.{FakePlan, LogicalPlanningTestSupport}
import org.neo4j.cypher.internal.util.v3_5.symbols.CTAny
import org.neo4j.cypher.internal.util.v3_5.test_helpers.CypherFunSuite
import org.neo4j.cypher.internal.v3_5.expressions._
import org.neo4j.cypher.internal.v3_5.logical.plans.{DropResult, LogicalPlan, Selection}

class SimplifySelectionsTest extends CypherFunSuite with LogicalPlanningTestSupport {
  test("should rewrite Selection(false, source) to DropResult(source)") {
    val source: LogicalPlan = FakePlan(Set.empty)
    val selection = Selection(Seq(False()(pos)), source)

    selection.endoRewrite(simplifySelections) should equal(
      DropResult( source))
  }

  test("should rewrite Selection(true, source) to source") {
    val source: LogicalPlan = FakePlan(Set.empty)
    val selection = Selection(Seq(True()(pos)), source)

    selection.endoRewrite(simplifySelections) should equal(source)
  }

  test("should not rewrite plans not obviously true or false") {
    val source: LogicalPlan = FakePlan(Set.empty)
    val selection = Selection(Seq(DummyExpression(CTAny)), source)

    selection.endoRewrite(simplifySelections) should equal(selection)
  }
}
