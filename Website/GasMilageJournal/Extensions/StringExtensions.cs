using System;
using System.Collections.Generic;
using System.Linq;

namespace GasMilageJournal.Extensions
{
    public static class StringExtensions
    {
        public static string Chomp(this string value, int len)
        {
            if (value == null) {
                return value;
            }

            if (value.Length <= len) {
                return value;
            }

            return value.Substring(0, value.Length - len);
        }
        public static Guid? ToNullableGuid(this string guid)
        {
            if (guid.IsEmpty()) {
                return null;
            }

            return new Guid(guid);
        }

        public static Guid ToGuid(this string guid)
        {
            if (guid.IsEmpty()) {
                throw new ArgumentNullException(nameof(guid));
            }

            return new Guid(guid);
        }

        public static string Digits(this string value)
        {
            return new string(value?.Where(c => c >= '0' && c <= '9').ToArray());
        }

        public static bool IsEmpty(this string value)
        {
            return string.IsNullOrWhiteSpace(value);
        }

        public static bool IsNotEmpty(this string value)
        {
            return !value.IsEmpty();
        }

        public static T Default<T>(this T value, T defaultValue = default(T))
        {
            if (value == null || value.ToString().IsEmpty()) {
                return defaultValue;
            }

            return value;
        }

        [Obsolete]
        public static List<string> AllBetween(this string value, char from, char to)
        {
            var result = new List<string>();

            return result;
        }

        public static string Between(this string value, char from, char to)
        {
            var result = string.Empty;
            bool read = false;
            bool stopped = false;

            foreach (var c in value.ToArray()) {
                if ((from != to && !read && c == to) || (read && c == to)) {
                    stopped = true;
                    break;
                }

                if (read) {
                    result += c;
                }

                if (c == from) {
                    read = true;
                }
            }

            // from and to were found
            if (read && stopped) {
                return result;
            }

            return string.Empty;
        }

        public static string ToPascal(this string value)
        {
            // Probably a constant value
            if (value.Contains("_") || value.IsAllUpper()) {
                value = value.ToLower();
                string[] parts = value.Split('_');
                var result = string.Empty;

                foreach (var part in parts) {
                    result += part.ToPascal();
                }

                return result;
            }

            return value.Capitalize();
        }

        public static bool IsAllUpper(this string value)
        {
            for (int i = 0; i < value.Length; i++) {
                if (char.IsLetter(value[i]) && !char.IsUpper(value[i]))
                    return false;
            }

            return true;
        }

        public static string Capitalize(this string value)
        {
            return char.ToUpper(value[0]) + value.Substring(1);
        }

        public static string AsDisplayName(this string value)
        {
            var result = string.Empty;

            foreach (string s in value.SplitOnUpper()) {
                result += s + " ";
            }

            return result.Trim();
        }

        public static string[] SplitOnUpper(this string value)
        {
            var result = new List<string>();
            var s = string.Empty;

            foreach (var c in value.ToList()) {
                if (char.IsLetter(c) && char.IsUpper(c)) {
                    if (s.IsNotEmpty()) {
                        result.Add(s);
                    }

                    s = string.Empty;
                }

                s += c;
            }

            result.Add(s);

            return result.ToArray();
        }
    }
}
