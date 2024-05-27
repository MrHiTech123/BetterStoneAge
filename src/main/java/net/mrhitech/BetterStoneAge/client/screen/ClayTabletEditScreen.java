//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mrhitech.BetterStoneAge.client.screen;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.font.TextFieldHelper.CursorStep;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

@OnlyIn(Dist.CLIENT)
public class ClayTabletEditScreen extends Screen {
    private static final int TEXT_WIDTH = 114;
    private static final int TEXT_HEIGHT = 128;
    private static final int IMAGE_WIDTH = 192;
    private static final int IMAGE_HEIGHT = 192;
    private static final Component EDIT_TITLE_LABEL = Component.translatable("book.editTitle");
    private static final Component FINALIZE_WARNING_LABEL = Component.translatable("book.finalizeWarning");
    private static final FormattedCharSequence BLACK_CURSOR;
    private static final FormattedCharSequence GRAY_CURSOR;
    private final Player owner;
    private final ItemStack book;
    private boolean isModified;
    private boolean isSigning;
    private int frameTick;
    private int currentPage;
    private final List<String> pages = Lists.newArrayList();
    private String title = "";
    private final TextFieldHelper pageEdit = new TextFieldHelper(this::getCurrentPageText, this::setCurrentPageText, this::getClipboard, this::setClipboard, (p_280853_) -> {
        return p_280853_.length() < 1024 && this.font.wordWrapHeight(p_280853_, 114) <= 128;
    });
    private final TextFieldHelper titleEdit = new TextFieldHelper(() -> {
        return this.title;
    }, (p_98175_) -> {
        this.title = p_98175_;
    }, this::getClipboard, this::setClipboard, (p_98170_) -> {
        return p_98170_.length() < 16;
    });
    private long lastClickTime;
    private int lastIndex = -1;
    private PageButton forwardButton;
    private PageButton backButton;
    private Button doneButton;
    private Button signButton;
    private Button finalizeButton;
    private Button cancelButton;
    private final InteractionHand hand;
    @Nullable
    private DisplayCache displayCache;
    private Component pageMsg;
    private final Component ownerText;

    public ClayTabletEditScreen(Player pOwner, ItemStack pBook, InteractionHand pHand) {
        super(GameNarrator.NO_TITLE);
        this.displayCache = ClayTabletEditScreen.DisplayCache.EMPTY;
        this.pageMsg = CommonComponents.EMPTY;
        this.owner = pOwner;
        this.book = pBook;
        this.hand = pHand;
        CompoundTag $$3 = pBook.getTag();
        if ($$3 != null) {
            List var10001 = this.pages;
            Objects.requireNonNull(var10001);
            BookViewScreen.loadPages($$3, var10001::add);
        }

        if (this.pages.isEmpty()) {
            this.pages.add("");
        }

        this.ownerText = Component.translatable("book.byAuthor", new Object[]{pOwner.getName()}).withStyle(ChatFormatting.DARK_GRAY);
    }

    private void setClipboard(String p_98148_) {
        if (this.minecraft != null) {
            TextFieldHelper.setClipboardContents(this.minecraft, p_98148_);
        }

    }

    private String getClipboard() {
        return this.minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
    }

    private int getNumPages() {
        return this.pages.size();
    }

    public void tick() {
        super.tick();
        ++this.frameTick;
    }

    protected void init() {
        this.clearDisplayCache();
        this.doneButton = (Button)this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_280851_) -> {
            this.minecraft.setScreen((Screen)null);
            this.saveChanges(false);
        }).bounds(this.width / 2 + 2, 196, 98, 20).build());
        this.finalizeButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("book.finalizeButton"), (p_280852_) -> {
            if (this.isSigning) {
                this.saveChanges(true);
                this.minecraft.setScreen((Screen)null);
            }

        }).bounds(this.width / 2 - 100, 196, 98, 20).build());
        this.cancelButton = (Button)this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (p_98157_) -> {
            if (this.isSigning) {
                this.isSigning = false;
            }

            this.updateButtonVisibility();
        }).bounds(this.width / 2 + 2, 196, 98, 20).build());
        int $$0 = (this.width - 192) / 2;
        boolean $$1 = true;
        this.updateButtonVisibility();
    }

    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
        this.clearDisplayCacheAfterPageChange();
    }

    private void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        } else {
            this.appendPageToBook();
            if (this.currentPage < this.getNumPages() - 1) {
                ++this.currentPage;
            }
        }

        this.updateButtonVisibility();
        this.clearDisplayCacheAfterPageChange();
    }

    private void updateButtonVisibility() {
        this.backButton.visible = !this.isSigning && this.currentPage > 0;
        this.forwardButton.visible = !this.isSigning;
        this.doneButton.visible = !this.isSigning;
        this.signButton.visible = !this.isSigning;
        this.cancelButton.visible = this.isSigning;
        this.finalizeButton.visible = this.isSigning;
        this.finalizeButton.active = !this.title.trim().isEmpty();
    }

    private void eraseEmptyTrailingPages() {
        ListIterator<String> $$0 = this.pages.listIterator(this.pages.size());

        while($$0.hasPrevious() && ((String)$$0.previous()).isEmpty()) {
            $$0.remove();
        }

    }

    private void saveChanges(boolean pPublish) {
        if (this.isModified) {
            this.eraseEmptyTrailingPages();
            this.updateLocalCopy(pPublish);
            int $$1 = this.hand == InteractionHand.MAIN_HAND ? this.owner.getInventory().selected : 40;
            this.minecraft.getConnection().send(new ServerboundEditBookPacket($$1, this.pages, pPublish ? Optional.of(this.title.trim()) : Optional.empty()));
        }
    }

    private void updateLocalCopy(boolean pSign) {
        ListTag $$1 = new ListTag();
        Stream var10000 = this.pages.stream().map(StringTag::valueOf);
        Objects.requireNonNull($$1);
        var10000.forEach(value -> $$1.add((Tag)value));
        if (!this.pages.isEmpty()) {
            this.book.addTagElement("pages", $$1);
        }

        if (pSign) {
            this.book.addTagElement("author", StringTag.valueOf(this.owner.getGameProfile().getName()));
            this.book.addTagElement("title", StringTag.valueOf(this.title.trim()));
        }

    }

    private void appendPageToBook() {
        if (this.getNumPages() < 100) {
            this.pages.add("");
            this.isModified = true;
        }
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else if (this.isSigning) {
            return this.titleKeyPressed(pKeyCode, pScanCode, pModifiers);
        } else {
            boolean $$3 = this.bookKeyPressed(pKeyCode, pScanCode, pModifiers);
            if ($$3) {
                this.clearDisplayCache();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean charTyped(char pCodePoint, int pModifiers) {
        if (super.charTyped(pCodePoint, pModifiers)) {
            return true;
        } else if (this.isSigning) {
            boolean $$2 = this.titleEdit.charTyped(pCodePoint);
            if ($$2) {
                this.updateButtonVisibility();
                this.isModified = true;
                return true;
            } else {
                return false;
            }
        } else if (SharedConstants.isAllowedChatCharacter(pCodePoint)) {
            this.pageEdit.insertText(Character.toString(pCodePoint));
            this.clearDisplayCache();
            return true;
        } else {
            return false;
        }
    }

    private boolean bookKeyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (Screen.isSelectAll(pKeyCode)) {
            this.pageEdit.selectAll();
            return true;
        } else if (Screen.isCopy(pKeyCode)) {
            this.pageEdit.copy();
            return true;
        } else if (Screen.isPaste(pKeyCode)) {
            this.pageEdit.paste();
            return true;
        } else if (Screen.isCut(pKeyCode)) {
            this.pageEdit.cut();
            return true;
        } else {
            TextFieldHelper.CursorStep $$3 = Screen.hasControlDown() ? CursorStep.WORD : CursorStep.CHARACTER;
            switch (pKeyCode) {
                case 257:
                case 335:
                    this.pageEdit.insertText("\n");
                    return true;
                case 259:
                    this.pageEdit.removeFromCursor(-1, $$3);
                    return true;
                case 261:
                    this.pageEdit.removeFromCursor(1, $$3);
                    return true;
                case 262:
                    this.pageEdit.moveBy(1, Screen.hasShiftDown(), $$3);
                    return true;
                case 263:
                    this.pageEdit.moveBy(-1, Screen.hasShiftDown(), $$3);
                    return true;
                case 264:
                    this.keyDown();
                    return true;
                case 265:
                    this.keyUp();
                    return true;
                case 266:
                    this.backButton.onPress();
                    return true;
                case 267:
                    this.forwardButton.onPress();
                    return true;
                case 268:
                    this.keyHome();
                    return true;
                case 269:
                    this.keyEnd();
                    return true;
                default:
                    return false;
            }
        }
    }

    private void keyUp() {
        this.changeLine(-1);
    }

    private void keyDown() {
        this.changeLine(1);
    }

    private void changeLine(int pYChange) {
        int $$1 = this.pageEdit.getCursorPos();
        int $$2 = this.getDisplayCache().changeLine($$1, pYChange);
        this.pageEdit.setCursorPos($$2, Screen.hasShiftDown());
    }

    private void keyHome() {
        if (Screen.hasControlDown()) {
            this.pageEdit.setCursorToStart(Screen.hasShiftDown());
        } else {
            int $$0 = this.pageEdit.getCursorPos();
            int $$1 = this.getDisplayCache().findLineStart($$0);
            this.pageEdit.setCursorPos($$1, Screen.hasShiftDown());
        }

    }

    private void keyEnd() {
        if (Screen.hasControlDown()) {
            this.pageEdit.setCursorToEnd(Screen.hasShiftDown());
        } else {
            DisplayCache $$0 = this.getDisplayCache();
            int $$1 = this.pageEdit.getCursorPos();
            int $$2 = $$0.findLineEnd($$1);
            this.pageEdit.setCursorPos($$2, Screen.hasShiftDown());
        }

    }

    private boolean titleKeyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        switch (pKeyCode) {
            case 257:
            case 335:
                if (!this.title.isEmpty()) {
                    this.saveChanges(true);
                    this.minecraft.setScreen((Screen)null);
                }

                return true;
            case 259:
                this.titleEdit.removeCharsFromCursor(-1);
                this.updateButtonVisibility();
                this.isModified = true;
                return true;
            default:
                return false;
        }
    }

    private String getCurrentPageText() {
        return this.currentPage >= 0 && this.currentPage < this.pages.size() ? (String)this.pages.get(this.currentPage) : "";
    }

    private void setCurrentPageText(String p_98159_) {
        if (this.currentPage >= 0 && this.currentPage < this.pages.size()) {
            this.pages.set(this.currentPage, p_98159_);
            this.isModified = true;
            this.clearDisplayCache();
        }

    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        this.setFocused((GuiEventListener)null);
        int $$4 = (this.width - 192) / 2;
        boolean $$5 = true;
        pGuiGraphics.blit(BookViewScreen.BOOK_LOCATION, $$4, 2, 0, 0, 192, 192);
        int $$9;
        int $$10;
        if (this.isSigning) {
            boolean $$6 = this.frameTick / 6 % 2 == 0;
            FormattedCharSequence $$7 = FormattedCharSequence.composite(FormattedCharSequence.forward(this.title, Style.EMPTY), $$6 ? BLACK_CURSOR : GRAY_CURSOR);
            int $$8 = this.font.width(EDIT_TITLE_LABEL);
            pGuiGraphics.drawString(this.font, EDIT_TITLE_LABEL, $$4 + 36 + (114 - $$8) / 2, 34, 0, false);
            $$9 = this.font.width($$7);
            pGuiGraphics.drawString(this.font, $$7, $$4 + 36 + (114 - $$9) / 2, 50, 0, false);
            $$10 = this.font.width(this.ownerText);
            pGuiGraphics.drawString(this.font, this.ownerText, $$4 + 36 + (114 - $$10) / 2, 60, 0, false);
            pGuiGraphics.drawWordWrap(this.font, FINALIZE_WARNING_LABEL, $$4 + 36, 82, 114, 0);
        } else {
            int $$11 = this.font.width(this.pageMsg);
            pGuiGraphics.drawString(this.font, this.pageMsg, $$4 - $$11 + 192 - 44, 18, 0, false);
            DisplayCache $$12 = this.getDisplayCache();
            LineInfo[] var15 = $$12.lines;
            $$9 = var15.length;

            for($$10 = 0; $$10 < $$9; ++$$10) {
                LineInfo $$13 = var15[$$10];
                pGuiGraphics.drawString(this.font, $$13.asComponent, $$13.x, $$13.y, -16777216, false);
            }

            this.renderHighlight(pGuiGraphics, $$12.selection);
            this.renderCursor(pGuiGraphics, $$12.cursor, $$12.cursorAtEnd);
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private void renderCursor(GuiGraphics pGuiGraphics, Pos2i pCursorPos, boolean pIsEndOfText) {
        if (this.frameTick / 6 % 2 == 0) {
            pCursorPos = this.convertLocalToScreen(pCursorPos);
            if (!pIsEndOfText) {
                int var10001 = pCursorPos.x;
                int var10002 = pCursorPos.y - 1;
                int var10003 = pCursorPos.x + 1;
                int var10004 = pCursorPos.y;
                Objects.requireNonNull(this.font);
                pGuiGraphics.fill(var10001, var10002, var10003, var10004 + 9, -16777216);
            } else {
                pGuiGraphics.drawString(this.font, "_", pCursorPos.x, pCursorPos.y, 0, false);
            }
        }

    }

    private void renderHighlight(GuiGraphics pGuiGraphics, Rect2i[] pHighlightAreas) {
        Rect2i[] var3 = pHighlightAreas;
        int var4 = pHighlightAreas.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Rect2i $$2 = var3[var5];
            int $$3 = $$2.getX();
            int $$4 = $$2.getY();
            int $$5 = $$3 + $$2.getWidth();
            int $$6 = $$4 + $$2.getHeight();
            pGuiGraphics.fill(RenderType.guiTextHighlight(), $$3, $$4, $$5, $$6, -16776961);
        }

    }

    private Pos2i convertScreenToLocal(Pos2i pScreenPos) {
        return new Pos2i(pScreenPos.x - (this.width - 192) / 2 - 36, pScreenPos.y - 32);
    }

    private Pos2i convertLocalToScreen(Pos2i pLocalScreenPos) {
        return new Pos2i(pLocalScreenPos.x + (this.width - 192) / 2 + 36, pLocalScreenPos.y + 32);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (super.mouseClicked(pMouseX, pMouseY, pButton)) {
            return true;
        } else {
            if (pButton == 0) {
                long $$3 = Util.getMillis();
                DisplayCache $$4 = this.getDisplayCache();
                int $$5 = $$4.getIndexAtPosition(this.font, this.convertScreenToLocal(new Pos2i((int)pMouseX, (int)pMouseY)));
                if ($$5 >= 0) {
                    if ($$5 == this.lastIndex && $$3 - this.lastClickTime < 250L) {
                        if (!this.pageEdit.isSelecting()) {
                            this.selectWord($$5);
                        } else {
                            this.pageEdit.selectAll();
                        }
                    } else {
                        this.pageEdit.setCursorPos($$5, Screen.hasShiftDown());
                    }

                    this.clearDisplayCache();
                }

                this.lastIndex = $$5;
                this.lastClickTime = $$3;
            }

            return true;
        }
    }

    private void selectWord(int pIndex) {
        String $$1 = this.getCurrentPageText();
        this.pageEdit.setSelectionRange(StringSplitter.getWordPosition($$1, -1, pIndex, false), StringSplitter.getWordPosition($$1, 1, pIndex, false));
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY)) {
            return true;
        } else {
            if (pButton == 0) {
                DisplayCache $$5 = this.getDisplayCache();
                int $$6 = $$5.getIndexAtPosition(this.font, this.convertScreenToLocal(new Pos2i((int)pMouseX, (int)pMouseY)));
                this.pageEdit.setCursorPos($$6, true);
                this.clearDisplayCache();
            }

            return true;
        }
    }

    private DisplayCache getDisplayCache() {
        if (this.displayCache == null) {
            this.displayCache = this.rebuildDisplayCache();
            this.pageMsg = Component.translatable("book.pageIndicator", new Object[]{this.currentPage + 1, this.getNumPages()});
        }

        return this.displayCache;
    }

    private void clearDisplayCache() {
        this.displayCache = null;
    }

    private void clearDisplayCacheAfterPageChange() {
        this.pageEdit.setCursorToEnd();
        this.clearDisplayCache();
    }

    private DisplayCache rebuildDisplayCache() {
        String $$0 = this.getCurrentPageText();
        if ($$0.isEmpty()) {
            return ClayTabletEditScreen.DisplayCache.EMPTY;
        } else {
            int $$1 = this.pageEdit.getCursorPos();
            int $$2 = this.pageEdit.getSelectionPos();
            IntList $$3 = new IntArrayList();
            List<LineInfo> $$4 = Lists.newArrayList();
            MutableInt $$5 = new MutableInt();
            MutableBoolean $$6 = new MutableBoolean();
            StringSplitter $$7 = this.font.getSplitter();
            $$7.splitLines($$0, 114, Style.EMPTY, true, (p_98132_, p_98133_, p_98134_) -> {
                int $$8 = $$5.getAndIncrement();
                String $$9 = $$0.substring(p_98133_, p_98134_);
                $$6.setValue($$9.endsWith("\n"));
                String $$10 = StringUtils.stripEnd($$9, " \n");
                Objects.requireNonNull(this.font);
                int $$11 = $$8 * 9;
                Pos2i $$12 = this.convertLocalToScreen(new Pos2i(0, $$11));
                $$3.add(p_98133_);
                $$4.add(new LineInfo(p_98132_, $$10, $$12.x, $$12.y));
            });
            int[] $$8 = $$3.toIntArray();
            boolean $$9 = $$1 == $$0.length();
            Pos2i $$13;
            int $$15;
            if ($$9 && $$6.isTrue()) {
                int var10003 = $$4.size();
                Objects.requireNonNull(this.font);
                $$13 = new Pos2i(0, var10003 * 9);
            } else {
                int $$11 = findLineFromPos($$8, $$1);
                $$15 = this.font.width($$0.substring($$8[$$11], $$1));
                Objects.requireNonNull(this.font);
                $$13 = new Pos2i($$15, $$11 * 9);
            }

            List<Rect2i> $$14 = Lists.newArrayList();
            if ($$1 != $$2) {
                $$15 = Math.min($$1, $$2);
                int $$16 = Math.max($$1, $$2);
                int $$17 = findLineFromPos($$8, $$15);
                int $$18 = findLineFromPos($$8, $$16);
                int $$19;
                int $$22;
                if ($$17 == $$18) {
                    Objects.requireNonNull(this.font);
                    $$19 = $$17 * 9;
                    $$22 = $$8[$$17];
                    $$14.add(this.createPartialLineSelection($$0, $$7, $$15, $$16, $$19, $$22));
                } else {
                    $$19 = $$17 + 1 > $$8.length ? $$0.length() : $$8[$$17 + 1];
                    Objects.requireNonNull(this.font);
                    $$14.add(this.createPartialLineSelection($$0, $$7, $$15, $$19, $$17 * 9, $$8[$$17]));

                    for($$22 = $$17 + 1; $$22 < $$18; ++$$22) {
                        Objects.requireNonNull(this.font);
                        int $$23 = $$22 * 9;
                        String $$24 = $$0.substring($$8[$$22], $$8[$$22 + 1]);
                        int $$25 = (int)$$7.stringWidth($$24);
                        Pos2i var10002 = new Pos2i(0, $$23);
                        Objects.requireNonNull(this.font);
                        $$14.add(this.createSelection(var10002, new Pos2i($$25, $$23 + 9)));
                    }

                    int var10004 = $$8[$$18];
                    Objects.requireNonNull(this.font);
                    $$14.add(this.createPartialLineSelection($$0, $$7, var10004, $$16, $$18 * 9, $$8[$$18]));
                }
            }

            return new DisplayCache($$0, $$13, $$9, $$8, (LineInfo[])$$4.toArray(new LineInfo[0]), (Rect2i[])$$14.toArray(new Rect2i[0]));
        }
    }

    static int findLineFromPos(int[] pLineStarts, int pFind) {
        int $$2 = Arrays.binarySearch(pLineStarts, pFind);
        return $$2 < 0 ? -($$2 + 2) : $$2;
    }

    private Rect2i createPartialLineSelection(String pInput, StringSplitter pSplitter, int p_98122_, int p_98123_, int p_98124_, int p_98125_) {
        String $$6 = pInput.substring(p_98125_, p_98122_);
        String $$7 = pInput.substring(p_98125_, p_98123_);
        Pos2i $$8 = new Pos2i((int)pSplitter.stringWidth($$6), p_98124_);
        int var10002 = (int)pSplitter.stringWidth($$7);
        Objects.requireNonNull(this.font);
        Pos2i $$9 = new Pos2i(var10002, p_98124_ + 9);
        return this.createSelection($$8, $$9);
    }

    private Rect2i createSelection(Pos2i pCorner1, Pos2i pCorner2) {
        Pos2i $$2 = this.convertLocalToScreen(pCorner1);
        Pos2i $$3 = this.convertLocalToScreen(pCorner2);
        int $$4 = Math.min($$2.x, $$3.x);
        int $$5 = Math.max($$2.x, $$3.x);
        int $$6 = Math.min($$2.y, $$3.y);
        int $$7 = Math.max($$2.y, $$3.y);
        return new Rect2i($$4, $$6, $$5 - $$4, $$7 - $$6);
    }

    static {
        BLACK_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.BLACK));
        GRAY_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.GRAY));
    }

    @OnlyIn(Dist.CLIENT)
    static class DisplayCache {
        static final DisplayCache EMPTY;
        private final String fullText;
        final Pos2i cursor;
        final boolean cursorAtEnd;
        private final int[] lineStarts;
        final LineInfo[] lines;
        final Rect2i[] selection;

        public DisplayCache(String pFullText, Pos2i pCursor, boolean pCursorAtEnd, int[] pLineStarts, LineInfo[] pLines, Rect2i[] pSelection) {
            this.fullText = pFullText;
            this.cursor = pCursor;
            this.cursorAtEnd = pCursorAtEnd;
            this.lineStarts = pLineStarts;
            this.lines = pLines;
            this.selection = pSelection;
        }

        public int getIndexAtPosition(Font pFont, Pos2i pCursorPosition) {
            int var10000 = pCursorPosition.y;
            Objects.requireNonNull(pFont);
            int $$2 = var10000 / 9;
            if ($$2 < 0) {
                return 0;
            } else if ($$2 >= this.lines.length) {
                return this.fullText.length();
            } else {
                LineInfo $$3 = this.lines[$$2];
                return this.lineStarts[$$2] + pFont.getSplitter().plainIndexAtWidth($$3.contents, pCursorPosition.x, $$3.style);
            }
        }

        public int changeLine(int pXChange, int pYChange) {
            int $$2 = ClayTabletEditScreen.findLineFromPos(this.lineStarts, pXChange);
            int $$3 = $$2 + pYChange;
            int $$7;
            if (0 <= $$3 && $$3 < this.lineStarts.length) {
                int $$4 = pXChange - this.lineStarts[$$2];
                int $$5 = this.lines[$$3].contents.length();
                $$7 = this.lineStarts[$$3] + Math.min($$4, $$5);
            } else {
                $$7 = pXChange;
            }

            return $$7;
        }

        public int findLineStart(int pLine) {
            int $$1 = ClayTabletEditScreen.findLineFromPos(this.lineStarts, pLine);
            return this.lineStarts[$$1];
        }

        public int findLineEnd(int pLine) {
            int $$1 = ClayTabletEditScreen.findLineFromPos(this.lineStarts, pLine);
            return this.lineStarts[$$1] + this.lines[$$1].contents.length();
        }

        static {
            EMPTY = new DisplayCache("", new Pos2i(0, 0), true, new int[]{0}, new LineInfo[]{new LineInfo(Style.EMPTY, "", 0, 0)}, new Rect2i[0]);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static class LineInfo {
        final Style style;
        final String contents;
        final Component asComponent;
        final int x;
        final int y;

        public LineInfo(Style pStyle, String pContents, int pX, int pY) {
            this.style = pStyle;
            this.contents = pContents;
            this.x = pX;
            this.y = pY;
            this.asComponent = Component.literal(pContents).setStyle(pStyle);
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class Pos2i {
        public final int x;
        public final int y;

        Pos2i(int pX, int pY) {
            this.x = pX;
            this.y = pY;
        }
    }
}
