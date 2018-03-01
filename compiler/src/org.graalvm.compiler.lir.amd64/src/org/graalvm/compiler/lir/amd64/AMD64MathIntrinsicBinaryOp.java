/*
 * Copyright (c) 2011, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.graalvm.compiler.lir.amd64;

import static org.graalvm.compiler.lir.LIRInstruction.OperandFlag.REG;

import org.graalvm.compiler.asm.amd64.AMD64MacroAssembler;
import org.graalvm.compiler.debug.GraalError;
import org.graalvm.compiler.lir.LIRInstructionClass;
import org.graalvm.compiler.lir.Opcode;
import org.graalvm.compiler.lir.asm.CompilationResultBuilder;
import org.graalvm.compiler.lir.gen.LIRGeneratorTool;

import jdk.vm.ci.meta.Value;

public final class AMD64MathIntrinsicBinaryOp extends AMD64LIRInstruction {
    public static final LIRInstructionClass<AMD64MathIntrinsicBinaryOp> TYPE = LIRInstructionClass.create(AMD64MathIntrinsicBinaryOp.class);

    public enum BinaryIntrinsicOpcode {
        POW
    }

    @Opcode private final BinaryIntrinsicOpcode opcode;
    @Def protected Value result;
    @Use protected Value input;
    @Use protected Value secondInput;
    @Temp({REG}) protected Value[] temps;

    public AMD64MathIntrinsicBinaryOp(LIRGeneratorTool tool, BinaryIntrinsicOpcode opcode, Value result, Value input, Value alternateInput) {
        super(TYPE);
        this.opcode = opcode;
        this.result = result;
        this.input = input;
        this.secondInput = alternateInput;
        switch (opcode) {
            case POW:
                temps = AMD64MathPow.temps.clone();
                break;
            default:
                throw GraalError.shouldNotReachHere();
        }
    }

    @Override
    public void emitCode(CompilationResultBuilder crb, AMD64MacroAssembler masm) {
        switch (opcode) {
            case POW:
                new AMD64MathPow().generate(masm, crb, result);
                break;
            default:
                throw GraalError.shouldNotReachHere();
        }
    }
}
