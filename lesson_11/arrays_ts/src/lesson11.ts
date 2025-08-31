/**
 * Provide the solution to LeetCode 1929 here:
 * https://leetcode.com/problems/concatenation-of-array
 */
export function getConcatenation(nums: number[]): number[] {
  const n = nums.length;
  const result = new Array(n * 2);
  for (let i = 0; i < n; i++) {
    result[i] = nums[i];
    result[i + n] = nums[i];
  }
  return result;
}

/**
 * Provide the solution to LeetCode 2942 here:
 * https://leetcode.com/problems/find-words-containing-character/
 */
export function findWordsContaining(words: string[], x: string): number[] {
  const result: number[] = [];
  const n = words.length;
  for (let i = 0; i < n; i++) {
    if (words[i].indexOf(x) !== -1) {
      result.push(i);
    }
  }
  return result;
}
