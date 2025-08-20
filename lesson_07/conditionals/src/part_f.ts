/**
 * Determines the grade letter based on a numeric score.
 * A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: below 60
 *
 * @param score
 * @returns
 */
export function getLetterGrade(score: number): string {
  
  if (score >= 90 && score <= 100) {
    return "A";
  } else if (score >= 80 && score < 90) {
    return "B";
  } else if (score >= 70 && score < 80) {
    return "C";
  } else if (score >= 60 && score < 70) {
    return "D";
  } else if (score < 60) {
    return "F";
  }
  if (score < 0 || score > 100) {
    return "Invalid score";
  }
  return "";
}

/**
 * Calculates the sum of all even numbers in the given array.
 *
 * @param numbers
 * @returns
 */
export function sumEvenNumbers(numbers: number[]): number {
  let sum = 0;
  for (let i = 0; i < numbers.length; i++) {
    if (numbers[i] % 2 === 0) {
      sum += numbers[i];
    }
  }
  return sum;
}

/**
 * Counts how many times a specific character appears in a string.
 *
 * @param text
 * @param character
 * @returns
 */
export function countCharacter(text: string, character: string): number {
  let count = 0;

  for (let i = 0; i < text.length; i++) {
    if (text[i] === character) {
      count++;
    }
  }
  return count;
}
