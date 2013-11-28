package org.zkoss.zss.ngmodel.impl;

import org.zkoss.zss.ngmodel.ModelEvent;
import org.zkoss.zss.ngmodel.NCellStyle;
import org.zkoss.zss.ngmodel.util.Validations;

public class RowImpl extends AbstractRow {
	private static final long serialVersionUID = 1L;
	
	private AbstractSheet sheet;

	private final BiIndexPool<AbstractCell> cells = new BiIndexPool<AbstractCell>();

	private AbstractCellStyle cellStyle;

	public RowImpl(AbstractSheet sheet) {
		this.sheet = sheet;
	}

	protected AbstractSheet getSheet() {
		checkOrphan();
		return sheet;
	}

	public int getIndex() {
		checkOrphan();
		return sheet.getRowIndex(this);
	}

	public boolean isNull() {
		return false;
	}

	public AbstractCell getCellAt(int columnIdx) {
		return getCellAt(columnIdx, true);
	}

	@Override
	AbstractCell getCellAt(int columnIdx, boolean proxy) {
		AbstractCell cellObj = cells.get(columnIdx);
		if (cellObj != null) {
			return cellObj;
		}
		checkOrphan();
		return proxy ? new CellProxyImpl(sheet, getIndex(), columnIdx) : null;
	}
	@Override
	AbstractCell getOrCreateCellAt(int columnIdx) {
		AbstractCell cellObj = cells.get(columnIdx);
		if (cellObj == null) {
			checkOrphan();
			cellObj = new CellImpl(this);
			cells.put(columnIdx, cellObj);
			// create column since we have cell of it
			sheet.getOrCreateColumnAt(columnIdx);
		}
		return cellObj;
	}
	@Override
	int getCellIndex(AbstractCell cell) {
		return cells.get(cell);
	}

	public int getStartCellIndex() {
		return cells.firstKey();
	}

	public int getEndCellIndex() {
		return cells.lastKey();
	}
	@Override
	protected void onModelEvent(ModelEvent event) {
		// TODO Auto-generated method stub

	}

	public void clearCell(int start, int end) {
		//clear before move relation
		for (AbstractCell cell : cells.subValues(start, end)) {
			cell.release();
		}
		cells.clear(start, end);
	}

	public void insertCell(int cellIdx, int size) {
		if (size <= 0)
			return;
		
		cells.insert(cellIdx, size);
	}

	public void deleteCell(int cellIdx, int size) {
		if (size <= 0)
			return;
		//clear before move relation
		for (AbstractCell cell : cells.subValues(cellIdx, cellIdx+size)) {
			cell.release();
		}
		
		cells.delete(cellIdx, size);
	}

	public String asString() {
		return String.valueOf(getIndex() + 1);
	}

	public void checkOrphan() {
		if (sheet == null) {
			throw new IllegalStateException("doesn't connect to parent");
		}
	}

	@Override
	public void release() {
		checkOrphan();
		for(AbstractCell cell:cells.values()){
			cell.release();
		}
		sheet = null;
	}

	public NCellStyle getCellStyle() {
		return getCellStyle(false);
	}

	public NCellStyle getCellStyle(boolean local) {
		if (local || cellStyle != null) {
			return cellStyle;
		}
		checkOrphan();
		return sheet.getBook().getDefaultCellStyle();
	}

	public void setCellStyle(NCellStyle cellStyle) {
		Validations.argNotNull(cellStyle);
		Validations.argInstance(cellStyle, CellStyleImpl.class);
		this.cellStyle = (CellStyleImpl) cellStyle;
	}

}
