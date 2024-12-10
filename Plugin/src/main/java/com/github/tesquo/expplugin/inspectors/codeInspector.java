package com.github.tesquo.expplugin.inspectors;

import com.github.tesquo.expplugin.EXPCode.Score;
import com.github.tesquo.expplugin.EXPCode.XPEvaluator;
import com.intellij.codeInspection.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;

public class codeInspector extends LocalInspectionTool {
    @Override
    public PsiElementVisitor buildVisitor(final ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitFile(PsiFile file) {
                String content = file.getText();
                if (file == null) {
                    System.out.println("file is null");
                    return;
                }
                XPEvaluator evaluator = new XPEvaluator(content);
                int score = evaluator.normalisedScore();
                holder.registerProblem(file, "XP Score: " + score);
                registerDetailedProblems(holder, file, evaluator);


            }

            private void registerDetailedProblems(ProblemsHolder holder, PsiFile file, XPEvaluator evaluator) {
                for (Score indentationScore : evaluator.scoreIndentation) {
                    PsiElement lineElement = getLineElement(file, indentationScore.line);
                    if (indentationScore.score > 0) {
                        ProblemDescriptor descriptor = holder.getManager().createProblemDescriptor(
                                lineElement,
                                indentationScore.reason,
                                false,
                                ProblemHighlightType.WEAK_WARNING,
                                false,
                                (LocalQuickFix) null);
                        holder.registerProblem(descriptor);
                    }
                }
                for (Score classScore : evaluator.scoreClassStructure) {
                    PsiElement lineElement = getLineElement(file, classScore.line - 1);
                    if (classScore.score > 0) {
                        ProblemDescriptor descriptor = holder.getManager().createProblemDescriptor(
                                lineElement,
                                classScore.reason,
                                false,
                                ProblemHighlightType.WEAK_WARNING,
                                false,
                                (LocalQuickFix) null);
                        holder.registerProblem(descriptor);
                    }
                }
                for (Score methodScore : evaluator.scoreMethodStructure) {
                    PsiElement lineElement = getLineElement(file, methodScore.line);
                    if (methodScore.score > 0) {
                        ProblemDescriptor descriptor = holder.getManager().createProblemDescriptor(
                                lineElement,
                                methodScore.reason,
                                false,
                                ProblemHighlightType.WEAK_WARNING,
                                false,
                                (LocalQuickFix) null);
                        holder.registerProblem(descriptor);
                    }
                }
            }
            private PsiElement getLineElement(PsiFile file, int lineNumber) {
                int offset = getOffsetAtLine(file, lineNumber);
                return offset >= 0 ? file.findElementAt(offset) : null;
            }
            private int getOffsetAtLine(PsiFile file, int lineNumber) {
                Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
                if (document != null && lineNumber >= 0 && lineNumber < document.getLineCount()) {
                    return document.getLineStartOffset(lineNumber);
                }
                return -1;
            }
        };
    }
}

