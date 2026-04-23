/**
 * Formats a backend error response into a human-readable string.
 * The Spring GlobalExceptionHandler returns JSON with fields:
 *   error, message, reason, suggestion, fields (map of field-level errors)
 */
export function formatBackendError(e: any): string {
  const err = e?.error;
  if (!err) return e?.message || 'An unexpected error occurred.';

  const parts: string[] = [];

  if (err.error)   parts.push(err.error);
  if (err.message) parts.push(err.message);
  if (err.reason)  parts.push(err.reason);

  if (err.fields && typeof err.fields === 'object') {
    const fieldMsgs = Object.entries(err.fields)
      .map(([k, v]) => `• ${k}: ${v}`)
      .join('  ');
    parts.push('Field errors — ' + fieldMsgs);
  }

  if (err.suggestion) parts.push('💡 ' + err.suggestion);

  return parts.filter(Boolean).join('  |  ') || 'Operation failed.';
}
