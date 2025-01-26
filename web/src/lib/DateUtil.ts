import { format } from 'src/lib/Format';

export function dateToDateStr(date: Date): string {
  return format(
    '%04d-%02d-%02d',
    date.getFullYear(),
    date.getMonth() + 1,
    date.getDate()
  );
}

export function dateToDatetimeStr(date: Date): string {
  return format(
    '%04d-%02d-%02d %02d:%02d:%02d',
    date.getFullYear(),
    date.getMonth() + 1,
    date.getDate(),
    date.getHours(),
    date.getMinutes(),
    date.getSeconds()
  );
}

export function apiDateToDateStr(date: string): string {
  return `${date.substring(0, 4)}-${date.substring(4, 6)}-${date.substring(
    6,
    8
  )}`;
}

export function datestrToApiDateStr(dateStr: string): string {
  return dateStr.replaceAll('-', '').trim();
}

export function yearMonthDayToApiDateStr(
  year: number,
  month: number,
  day: number
): string {
  return format('%04d%02d%02d', year, month, day);
}

export function dateToApiDateStr(date: Date): string {
  return format(
    '%04d%02d%02d',
    date.getFullYear(),
    date.getMonth() + 1,
    date.getDate()
  );
}
