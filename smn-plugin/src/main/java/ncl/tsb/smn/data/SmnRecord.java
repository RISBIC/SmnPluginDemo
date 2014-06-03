/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Immutable value class whose instances represent individual records from the Speed Management Network.
 */
public class SmnRecord {
	private final Long id;

	private final Integer classCode;

	private final Integer direction;

	private final Integer lane;

	private final Integer outstationId;

	private final Integer site;

	private final Integer speed;

	private final Long recordTime;

	private final Long insertTime;

	public SmnRecord(final Long id, final Integer classCode, final Integer direction, final Integer lane, final Integer outstationId, final Integer site, final Integer speed, final Long recordTime, final Long insertTime) {
		this.id = id;
		this.classCode = classCode;
		this.direction = direction;
		this.lane = lane;
		this.outstationId = outstationId;
		this.site = site;
		this.speed = speed;
		this.recordTime = recordTime;
		this.insertTime = insertTime;
	}

	public SmnRecord(final ResultSet resultSet) {
		try {
			this.id = resultSet.getLong("_id");
			this.classCode = resultSet.getInt("classcode");
			this.direction = resultSet.getInt("direction");
			this.lane = resultSet.getInt("lane");
			this.outstationId = resultSet.getInt("outstationid");
			this.site = resultSet.getInt("site");
			this.speed = resultSet.getInt("speed");
			this.recordTime = resultSet.getLong("time");
			this.insertTime = resultSet.getLong("inserttime");
		} catch (final SQLException e) {
			throw new IllegalArgumentException("SQLException while building SmnRecord.", e);
		}
	}

	public Long getId() {
		return id;
	}

	public Integer getClassCode() {
		return classCode;
	}

	public Integer getDirection() {
		return direction;
	}

	public Integer getLane() {
		return lane;
	}

	public Integer getOutstationId() {
		return outstationId;
	}

	public Integer getSite() {
		return site;
	}

	public Integer getSpeed() {
		return speed;
	}

	public Long getRecordTime() {
		return recordTime;
	}

	public Long getInsertTime() {
		return insertTime;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SmnRecord{");
		sb.append("id=").append(id);
		sb.append(", classCode=").append(classCode);
		sb.append(", direction=").append(direction);
		sb.append(", lane=").append(lane);
		sb.append(", outstationId=").append(outstationId);
		sb.append(", site=").append(site);
		sb.append(", speed=").append(speed);
		sb.append(", recordTime=").append(recordTime);
		sb.append(", insertTime=").append(insertTime);
		sb.append('}');

		return sb.toString();
	}

	/**
	 * Generate a dummy SMN Record with randomised data
	 *
	 * @return An instance of SmnRecord with randomised fields.
	 */
	public static SmnRecord randomDummy() {
		return new SmnRecord(1L, r(1, 4), r(0, 1), r(0, 5), r(1, 129), 0, r(30, 100), System.currentTimeMillis(), System.currentTimeMillis());
	}

	private static Integer r(final Integer min, final Integer max) {
		return (int) (min + (Math.random() * (max - min)));
	}
}