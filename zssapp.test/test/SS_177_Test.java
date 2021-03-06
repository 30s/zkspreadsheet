import org.zkoss.ztl.JQuery;

/**
 * Note: Please remember to modify this case if you change content of ZSSDEMO.
 * @author phoenix
 *
 */
public class SS_177_Test extends SSAbstractTestCase {

    @Override
    protected void executeTest() {
        // Select source cell
        JQuery cell_J_13 = getSpecifiedCell(9, 12);
        clickCell(cell_J_13);
        clickCell(cell_J_13);
        
        // Ctrl + C
        keyDownNative(CTRL);
        waitResponse();
        keyDownNative(C);
        waitResponse();
        keyUpNative(C);
        waitResponse();
        keyUpNative(CTRL);
        waitResponse();
        
        // Right click target cell
        JQuery cell_L_13 = loadTargetCell();
        clickCell(cell_L_13);
        rightClickCell(cell_L_13);
        waitResponse();
        
        // Click Paste Special on the context menu
        click(jq("$pasteSpecial"));
        waitResponse();
        
        // Choose All
        click(jq("$all input[id*=real]"));
        waitResponse();
        click(jq("$okBtn"));
        waitResponse();
        
        // Verify
        String formulaBarValue = jq("$formulaEditor").val();
        verifyEquals("Incorrect value: " + formulaBarValue, "=+K13", formulaBarValue);
    }

    private JQuery loadTargetCell() {
        return getSpecifiedCell(11, 12);
    }

}
